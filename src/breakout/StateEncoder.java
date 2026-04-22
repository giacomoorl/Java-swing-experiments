package breakout;
/*
* CLASSE CHE FUNGE DA INTEMEDIARIO TRA IL GAMESTATE 
* E IL RESTO DEL SISTEMA
*/
public class StateEncoder {
    // CODIFICA LO STATO IN UN NUMERO CHE LO RIASSUME E LO RITORNA 
    public int encode(GameState stato) {

        Pallina pallina = stato.getPallina();
        Asticella asticella = stato.getAsticella();

        // POSIZIONE PALLINA DIVISA IN 5 COLONNE
        int col = (int)(pallina.dammiX() / (1600.0 / 5));
        col = Math.max(0, Math.min(4, col));

        // DIREZIONE VERTICALE DELLA PALLINA 1 = GIÙ E 0 = SU
        int direzioneY = pallina.dammiDY() > 0 ? 1 : 0;

        // VELOCITÀ ORIZZONTALE IN 4 CATEGORIE
        double dx = pallina.dammiDX();
        int velX;
        if(dx < -2) 
            velX = 0;
        else if(dx < 0) 
            velX = 1;
        else if(dx < 2) 
            velX = 2;
        else 
            velX = 3;

        // DISTANZA TRA PADDLE E PALLINA DIVISA IN 5 VALORI
        double centroPalla = pallina.dammiX() + 10;
        double centroPaddle = asticella.dammiX() + asticella.dammiLarghezza()/2;
        double diff = centroPalla - centroPaddle;

        int rel;
        if(diff < -60) 
            rel = 0;
        else if(diff < -20) 
            rel = 1;
        else if(diff < 20) 
            rel = 2;
        else if(diff < 60) 
            rel = 3;
        else 
            rel = 4;

        return col * (2 * 4 * 5)
             + direzioneY * (4 * 5)
             + velX * 5
             + rel;
    }
}


