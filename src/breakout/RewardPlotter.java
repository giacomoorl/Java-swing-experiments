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
        new javax.swing.Timer(1000, e -> repaint()).start();
    }

    private List<Double> leggi() {
        List<Double> r = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("rewards.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                r.add(Double.parseDouble(line));
            }
        } catch (Exception e) {}

        return r;
    }

    // MEDIA MOBILE per rendere il grafico leggibile
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

        // smoothing (IMPORTANTISSIMO)
        List<Double> r = media(dati, 50);

        int w = getWidth();
        int h = getHeight();

        int margin = 40;

        // trova min e max
        double max = Double.NEGATIVE_INFINITY;
        double min = Double.POSITIVE_INFINITY;

        for (double v : r) {
            if (v > max) max = v;
            if (v < min) min = v;
        }

        if (max == min) return;

        Graphics2D g2 = (Graphics2D) g;

        // ===== SFONDO =====
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, w, h);

        // ===== ASSI =====
        g2.setColor(Color.WHITE);

        // asse X
        g2.drawLine(margin, h - margin, w - margin, h - margin);

        // asse Y
        g2.drawLine(margin, margin, margin, h - margin);

        // ===== ETICHETTE =====
        g2.drawString("Episodi", w / 2, h - 5);
        g2.drawString("Reward", 5, h / 2);

        // ===== SCALA Y =====
        g2.drawString(String.format("%.0f", max), 5, margin);
        g2.drawString(String.format("%.0f", min), 5, h - margin);

        // ===== GRAFICO =====
        g2.setColor(Color.GREEN);

        for (int i = 1; i < r.size(); i++) {

            int x1 = margin + (i - 1) * (w - 2 * margin) / r.size();
            int x2 = margin + i * (w - 2 * margin) / r.size();

            int y1 = h - margin - (int)((r.get(i - 1) - min) / (max - min) * (h - 2 * margin));
            int y2 = h - margin - (int)((r.get(i) - min) / (max - min) * (h - 2 * margin));

            g2.drawLine(x1, y1, x2, y2);
        }
    }
}