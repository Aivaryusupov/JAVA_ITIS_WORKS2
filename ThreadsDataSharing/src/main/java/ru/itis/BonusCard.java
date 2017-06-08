package ru.itis;

public class BonusCard {

    private int bonuses;

    public BonusCard(int bonuses) {
        this.bonuses = bonuses;
    }

    public int getBonuses() {
        return bonuses;
    }

    public boolean use(int n) {
        if (bonuses >= n) {
            bonuses -= n;
            System.out.println(bonuses + " left");
            return true;
        } else {
            System.out.println("OOPS");
            return false;
        }
    }
}
