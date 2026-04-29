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
* CLASSE CHE DISEGNA IL GRAFICO SULLO SCHERMO,
* NELLA QUALE SI PUÒ VEDERE COME VARIA LA REWARD 
* ALL'AUMENTARE DEL NUMERO DI EPISODI
*/
public class RewardPlotter extends JPanel {
    // COSTRUTTORE
    public RewardPlotter(){
        System.out.println("RewardPlot creato");
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
            for (int j = start; j <= i; j++) 
                sum += dati.get(j);
            out.add(sum / (i - start + 1));
        }
        return out;
    }
    // DISEGNA TUTTO 
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // LEGGE LE REWARD DA FILE E LI ASSEGNA ALLA LIST DI DATI
        List<Double> dati = leggi();
        if (dati.size() < 2) return;
        // NELLA LISTA METTE LA MEDIA DEGLI ULTIMI 20 REWARD LETTI DA FILE 
        List<Double> mediaMobile = media(dati, 20);
        int w = getWidth();
        int h = getHeight();
        int margin = 50;
        double maxAbs = Math.max( Math.abs(Collections.max(dati)), Math.abs(Collections.min(dati)));
        Graphics2D g2 = (Graphics2D) g;
        // SFONDO
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, w, h);
        // ===== LINEA ZERO (REWARD = 0) =====
        g2.setColor(Color.DARK_GRAY);
        g2.drawLine(margin, h / 2, w - margin, h / 2);
        // TESTO INFO
        double ultimo = dati.get(dati.size() - 1);
        double mediaFinale = mediaMobile.get(mediaMobile.size() - 1);
        
        g2.setColor(Color.WHITE);
        g2.drawString("Ultima Reward: " + (int)ultimo, 50, 20);
        g2.drawString("Media: " + (int)mediaFinale, 200, 20);
        g2.drawString("Episodio: " + dati.size(), 350, 20);
        // DISEGNA GLI ASSI X ( EPISODI ) E Y ( REWARD )
        g2.drawLine(margin, h - margin, w - margin, h - margin);
        g2.drawLine(margin, margin, margin, h - margin);
        // ETICHETTA GLI ASSI
        g2.drawString("Episodi", w / 2, h - 10);
        g2.drawString("Reward", 5, h / 2);
        // CALCOLA OGNI QUANTO METTERE I VALORI NEL ASSE DEGLI EPISODI
        double stepX = (double)(w - 2 * margin) / (dati.size() - 1);
        // ETICHETTA I VALORI NELL'ASSE X DEGLI EPISODI
        int stepLabel = Math.max(1, dati.size() / 10);

        for (int i = 0; i < dati.size(); i += stepLabel) {
            int x = (int)(margin + i * stepX);
            g2.drawLine(x, h - margin - 5, x, h - margin + 5);
            g2.drawString(String.valueOf(i), x - 10, h - margin + 20);
        }
        // LINEA VERDE (reward reale) 
        g2.setColor(Color.GREEN);

        for (int i = 1; i < dati.size(); i++) {
            int x1 = (int)(margin + (i - 1) * stepX);
            int x2 = (int)(margin + i * stepX);

            int y1 = (int)(h/2 - (dati.get(i - 1) / maxAbs) * (h/2 - margin));
            int y2 = (int)(h/2 - (dati.get(i) / maxAbs) * (h/2 - margin));
            
            g2.drawLine(x1, y1, x2, y2);
        }
        // LINEA ROSSA (media mobile) 
        g2.setColor(Color.RED);

        for (int i = 1; i < mediaMobile.size(); i++) {
            int x1 = (int)(margin + (i - 1) * stepX);
            int x2 = (int)(margin + i * stepX);

            int y1 = (int)(h/2 - (mediaMobile.get(i - 1) / maxAbs) * (h/2 - margin));
            int y2 = (int)(h/2 - (mediaMobile.get(i) / maxAbs) * (h/2 - margin));

            g2.drawLine(x1, y1, x2, y2);
        }
    }
}