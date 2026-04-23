package breakout;

import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.io.BufferedReader;
import java.io.FileReader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/*
* CLASSE CHE DISEGNA IL GRAFICO SULLO SCHERMO
* NELLA QUALE SI PUÒ VEDERE COME VARIA LA REWARD 
* ALL'AUMENTARE DEL NUMERO DI EPISODI
*/
public class RewardPlotter extends JPanel {
    // COSTRUTTORE
    public RewardPlotter() {
        setPreferredSize(new Dimension(800, 300));
        setBackground(Color.BLACK);
        // AGGIORNA OGNI 1000 MS ( 1 SECONDO )
        new Timer(1000, e -> repaint()).start();
    }
    // LEGGE LE REWARD DA FILE 
    private List<Double> leggi() {
        List<Double> dati = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("rewards.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                dati.add(Double.parseDouble(line));
            }
        } 
        catch (Exception e) {}

        return dati;
    }
    // CALCOLA LA MEDIA
    private List<Double> media(List<Double> dati, int window) {
        List<Double> out = new ArrayList<>();

        for (int i = 0; i < dati.size(); i++) {
            int start = Math.max(0, i - window);
            double sum = 0;

            for (int j = start; j <= i; j++) {
                sum += dati.get(j);
            }

            out.add(sum / (i - start + 1));
        }

        return out;
    }
    // DISEGNA TUTTO 
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        List<Double> dati = leggi();
        if (dati.size() < 2) return;

        // PRENDE ULTIMI 200
        int maxPoints = 200;
        if (dati.size() > maxPoints) {
            dati = dati.subList(dati.size() - maxPoints, dati.size());
        }

        List<Double> smooth = media(dati, 20);

        int w = getWidth();
        int h = getHeight();
        int margin = 40;

        // TROVA MIN E MAX
        double min = Collections.min(dati);
        double max = Collections.max(dati);

        if (max == min) 
            return;

        Graphics2D g2 = (Graphics2D) g;

        // SFONDO
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, w, h);
        // INFO TESTO
        double ultimo = dati.get(dati.size() - 1);
        double mediaFinale = smooth.get(smooth.size() - 1);

        g2.setColor(Color.WHITE);
        g2.drawString("Ultimo: " + (int)ultimo, 50, 20);
        g2.drawString("Media: " + (int)mediaFinale, 200, 20);
        // ASSI
        g2.setColor(Color.WHITE);
        g2.drawLine(margin, h - margin, w - margin, h - margin);
        g2.drawLine(margin, margin, margin, h - margin);

        g2.drawString("Episodi", w / 2, h - 5);
        g2.drawString("Reward", 5, h / 2);

        double stepX = (double)(w - 2 * margin) / (dati.size() - 1);

        // ===== LINEA VERDE (dati reali) =====
        g2.setColor(Color.GREEN);

        for (int i = 1; i < dati.size(); i++) {
            int x1 = (int)(margin + (i - 1) * stepX);
            int x2 = (int)(margin + i * stepX);

            int y1 = h - margin - (int)((dati.get(i - 1) - min) / (max - min) * (h - 2 * margin));
            int y2 = h - margin - (int)((dati.get(i) - min) / (max - min) * (h - 2 * margin));

            g2.drawLine(x1, y1, x2, y2);
        }

        // ===== LINEA ROSSA (media) =====
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