package com.sumsub.idensic.model;

public class Level {
   private final String id;
   private final String name;
   private final boolean action;

   public Level(String id, String name, Boolean action) {
      this.id = id;
      this.name = name;
      this.action = action;
   }

   public String getId() {
      return id;
   }

   public String getName() {
      return name;
   }

   public boolean isAction() {
      return action;
   }
}
