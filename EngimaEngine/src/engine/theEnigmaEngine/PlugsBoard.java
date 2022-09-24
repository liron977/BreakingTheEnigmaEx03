package engine.theEnigmaEngine;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class PlugsBoard  implements Serializable{
 private String keybord;
 private int amountOfMaximumSwappingPairs;
 private List<Pair> pairsOfSwappingCharacter = new ArrayList<>();

 public PlugsBoard(String keybord,List<Pair> pairsOfSwappingLetters){
  this.keybord=keybord;
  amountOfMaximumSwappingPairs=keybord.length()/2;
  this.pairsOfSwappingCharacter=pairsOfSwappingLetters;

 }
 public String getSwappedCharacter(String str) {
  for (Pair pair: pairsOfSwappingCharacter) {
       if(pair.getEntry().equals(str)){
        return pair.getExit();
       }
       else if(pair.getExit().equals(str)){
        return pair.getEntry();
   }
  }
  return str;
 }

 public void initEmptyPlugBoard(){
  amountOfMaximumSwappingPairs=0;
  pairsOfSwappingCharacter = new ArrayList<>();
 }

 public List<String> getStringPairsOfSwappingCharacter(){
  List<String> StringPairsOfSwappingCharacter=new ArrayList<>();
  for (Pair pair:pairsOfSwappingCharacter) {
   StringPairsOfSwappingCharacter.add(pair.toString());
  }
  return StringPairsOfSwappingCharacter;
 }

 public int getAmountOfMaximumSwappingPairs() {
  return amountOfMaximumSwappingPairs;
 }

 public List<Pair> getPairsOfSwappingCharacter() {
  return pairsOfSwappingCharacter;
 }

 public void setPairsOfSwappingCharacter(List<Pair> pairsOfSwappingCharacter){
  this.pairsOfSwappingCharacter=pairsOfSwappingCharacter;
 }
}