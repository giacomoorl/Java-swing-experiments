package breakout;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class RewardPlotter extends JPanel {

    public RewardPlotter() {
        setPreferredSize(new Dimension(800, 300));
        setBackground(Color.BLACK);

        // aggiorna ogni secondo
        new Timer(1000, e -> repaint()).start();
    }

    private List<Double> leggi() {
        List<Double> r = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("rewards.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                r.add(Double.parseDouble(line));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return r;
    }

    // MEDIA MOBILE
    private List<Double> media(List<Double> dati, int window) {
        List<Double> out = new ArrayList<>();

        for (int i = 0; i < dati.size(); i++) {
            int start = Math.max(0, i - window);
            double sum = 0;
            int count = 0;

            for (int j = start; j <= i; j++) {
                sum += dati.get(j);
                count++;
            }

            out.add(sum / count);
        }

        return out;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        List<Double> dati = leggi();
        if (dati.size() < 2) return;

        // limita punti (performance)
        int maxPoints = 500;
        if (dati.size() > maxPoints) {
            dati = dati.subList(dati.size() - maxPoints, dati.size());
        }

        List<Double> smooth = media(dati, 50);

        int w = getWidth();
        int h = getHeight();
        int margin = 40;

        double max = Double.NEGATIVE_INFINITY;
        double min = Double.POSITIVE_INFINITY;

        for (double v : dati) {
            if (v > max) max = v;
            if (v < min) min = v;
        }

        if (max == min) return;

        Graphics2D g2 = (Graphics2D) g;

        // sfondo
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, w, h);

        // assi
        g2.setColor(Color.WHITE);
        g2.drawLine(margin, h - margin, w - margin, h - margin);
        g2.drawLine(margin, margin, margin, h - margin);

        g2.drawString("Episodi", w / 2, h - 5);
        g2.drawString("Reward", 5, h / 2);

        // scala Y (5 tacche)
        for (int i = 0; i <= 5; i++) {
            double val = min + i * (max - min) / 5;
            int y = h - margin - (int)((val - min) / (max - min) * (h - 2 * margin));
            g2.drawString(String.format("%.0f", val), 5, y);
        }

        double stepX = (double)(w - 2 * margin) / (dati.size() - 1);

        // scala X
        int step = Math.max(1, dati.size() / 10);
        for (int i = 0; i < dati.size(); i += step) {
            int x = (int)(margin + i * stepX);
            g2.drawString(String.valueOf(i), x, h - margin + 15);
        }

        // ===== DATI REALI (verde) =====
        g2.setColor(Color.GREEN);

        for (int i = 1; i < dati.size(); i++) {
            int x1 = (int)(margin + (i - 1) * stepX);
            int x2 = (int)(margin + i * stepX);

            int y1 = h - margin - (int)((dati.get(i - 1) - min) / (max - min) * (h - 2 * margin));
            int y2 = h - margin - (int)((dati.get(i) - min) / (max - min) * (h - 2 * margin));

            g2.drawLine(x1, y1, x2, y2);
        }

        // ===== MEDIA (rosso) =====
        g2.setColor(Color.RED);

        for (int i = 1; i < smooth.size(); i++) {
            int x1 = (int)(margin + (i - 1) * stepX);
            int x2 = (int)(margin + i * stepX);

            int y1 = h - margin - (int)((smooth.get(i - 1) - min) / (max - min) * (h - 2 * margin));
            int y2 = h - margin - (int)((smooth.get(i) - min) / (max - min) * (h - 2 * margin));

            g2.drawLine(x1, y1, x2, y2);
        }
    }
}