package engine.theEnigmaEngine;

public class BattleField {
   private String battleName;
   private int allies;
   private String level;
   public BattleField(String battleName, int allies, String level){
       this.battleName=battleName;
       this.allies=allies;
       this.level=level;
   }
    public String getLevel() {
        return level;
    }
    public String getBattleName() {
        return battleName;
    }
    public int getAllies() {
        return allies;
    }
}
