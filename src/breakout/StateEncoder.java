package breakout;

public class StateEncoder {

    public int encode(GameState stato) {

        Pallina pallina = stato.getPallina();
        Asticella asticella = stato.getAsticella();

        // ===== 1. POSIZIONE PALLINA (5 colonne) =====
        int col = (int)(pallina.dammiX() / (1600.0 / 5));
        col = Math.max(0, Math.min(4, col));

        // ===== 2. DIREZIONE VERTICALE =====
        int dy = pallina.dammiDY() > 0 ? 1 : 0;

        // ===== 3. VELOCITÀ ORIZZONTALE =====
        double dx = pallina.dammiDX();
        int velX;
        if(dx < -2) velX = 0;
        else if(dx < 0) velX = 1;
        else if(dx < 2) velX = 2;
        else velX = 3;

        // ===== 4. DISTANZA RELATIVA =====
        double centroPalla = pallina.dammiX() + 10;
        double centroPaddle = asticella.dammiX() + asticella.dammiLarghezza()/2;
        double diff = centroPalla - centroPaddle;

        int rel;
        if(diff < -60) rel = 0;
        else if(diff < -20) rel = 1;
        else if(diff < 20) rel = 2;
        else if(diff < 60) rel = 3;
        else rel = 4;

        return col * (2 * 4 * 5)
             + dy * (4 * 5)
             + velX * 5
             + rel;
    }
}


