package engine.validator;


import schemaGenerated.CTEEnigma;
import schemaGenerated.CTEPositioning;
import schemaGenerated.CTERotor;

import java.util.*;

public class XmlRotorValidator implements Validator{

    private CTEEnigma enigmaDescriptor;
    private List<Exception> errors;
    private String keyboardInput;

    public XmlRotorValidator(CTEEnigma enigmaDescriptor) {
        this.enigmaDescriptor = enigmaDescriptor;
        errors=new ArrayList<>();
        this.keyboardInput = enigmaDescriptor.getCTEMachine().getABC().trim();
    }
    @Override
    public void validate() {
        isRotorsCountEqualsToExistsRotorsAmount();
        isEachRotorHasUniqId();
        isRotorsMappedIsLegal();
        isRotorsAmountOfSignalIsLegal();
        isRotorsSignalsAreValid();
        isNotchPositionLegal();
        isRotorsAmountIsLegal();
    }
    @Override
    public List<Exception> getListOfException() {
        return errors;
    }
    private void isRotorsAmountIsLegal(){
        int rotorsAmountFromFile=enigmaDescriptor.getCTEMachine().getRotorsCount();
        if(rotorsAmountFromFile<2){
            errors.add(new Exception("The rotors amount is not valid,you should have at least 2 rotors in the field rotors-count"));
        }
        if(rotorsAmountFromFile>99){
            errors.add(new Exception("The rotors amount is not valid,you should have maximum 99 rotors in the field rotors-count"));
        }
    }
    private int countRotorsFromFile(){
        List<CTERotor> cteRotors=enigmaDescriptor.getCTEMachine().getCTERotors().getCTERotor();
        int count=0;
        for (CTERotor cteRotor:cteRotors) {
            count++;
        }
        return count;
    }
    private void isRotorsCountEqualsToExistsRotorsAmount(){
        int rotorsAmountFromFile=enigmaDescriptor.getCTEMachine().getRotorsCount();
        int countedRotors=countRotorsFromFile();
        if(rotorsAmountFromFile>countedRotors){
            errors.add(new Exception("The amount of rotors to use that you entered in the field rotors-count is [" +rotorsAmountFromFile +"] and its more than the amount of rotors in the file :"+countedRotors));
        }
    }
    private void isEachRotorHasUniqId(){
        HashMap<Integer, Integer> idHashMap = new HashMap<>();
        List<CTERotor> cteRotors=enigmaDescriptor.getCTEMachine().getCTERotors().getCTERotor();
        int previousId=0;
        for (CTERotor cteRotor:cteRotors) {
            if((idHashMap.get(cteRotor.getId())!=null)&&(idHashMap.get(cteRotor.getId())>0)){
                errors.add(new Exception("Each rotor id must be uniq," +
                        "the id ["+ cteRotor.getId() +"] appear more than 1 time"));
            }
            idHashMap.put(cteRotor.getId(),1);
        }
        SortedSet<Integer> keys = new TreeSet<>(idHashMap.keySet());
        for (Integer key : keys) {
            if (key == 0) {
                errors.add(new Exception("The rotors`s id should be a running counter starting from 1,you have id [" + 0 + "]"));
            } else {
                if (key != previousId + 1&&previousId != 0) {

                        errors.add(new Exception("The rotors`s id should be a running counter starting from 1," +
                                "it can be unorganized." + "you have id [" + previousId + "] and the next id is [" + key + "]"));
                }
                previousId = key;
            }
        }
    }
    private void isRotorSignalsAreValid(CTERotor cteRotor){
        String left,right;
        List<CTEPositioning> ctePositioning=cteRotor.getCTEPositioning();
        for (CTEPositioning positing:ctePositioning) {
            left=positing.getLeft().toUpperCase();
            right=positing.getRight().toUpperCase();
            if(!(keyboardInput.toUpperCase().contains(left.toUpperCase()))){
                errors.add(new Exception("The signal "+left.toUpperCase()+ " on the left field in rotor ["+cteRotor.getId()+"] is illegal,the signals should be from the following ABC only: "+ keyboardInput ));
            }
            if((!keyboardInput.toUpperCase().contains(right.toUpperCase()))){
                errors.add(new Exception("The signal "+right.toUpperCase()+ " on the right field in rotor ["+cteRotor.getId()+"] is illegal,the signals should be from the following ABC only: "+keyboardInput ));

            }
        }
    }
    private void isRotorsSignalsAreValid(){
        List<CTERotor> cteRotors=enigmaDescriptor.getCTEMachine().getCTERotors().getCTERotor();
        for (CTERotor cteRotor:cteRotors) {
            isRotorSignalsAreValid(cteRotor);
        }
    }
    private void isRotorHasDoubleMapping(CTERotor cteRotor){
        String left,right;
        HashMap<String, Integer> leftSignalMap = new HashMap<>();
        HashMap<String, Integer> rightSignalMap = new HashMap<>();
        List<CTEPositioning> ctePositioning=cteRotor.getCTEPositioning();
        for (CTEPositioning positing:ctePositioning) {
            left=positing.getLeft().toUpperCase();
            if((leftSignalMap.get(left)!=null)&&(leftSignalMap.get(left)!=0)) {
                errors.add(new Exception("The rotor ["+cteRotor.getId()+"] is illegal,rotor can not have double mapping the signal ["+left+ "] in the left field is already mapped"));
            }
            leftSignalMap.put(left,1);
            right=positing.getRight().toUpperCase();
            if((rightSignalMap.get(right)!=null)&&(rightSignalMap.get(right)!=0)) {
                errors.add(new Exception("The rotor ["+cteRotor.getId()+"] is illegal,rotor can not have double mapping the signal ["+right+ "] in the right field is already mapped" ));
            }
            rightSignalMap.put(right,1);
        }
    }
    private void isRotorsAmountOfSignalIsLegal(){
        List<CTERotor> cteRotors=enigmaDescriptor.getCTEMachine().getCTERotors().getCTERotor();
        int amount=0;
        for (CTERotor cteRotor:cteRotors) {
            List<CTEPositioning> ctePositioning=cteRotor.getCTEPositioning();
            amount=0;
            for (CTEPositioning positing:ctePositioning) {
                amount++;
            }
            if(amount<keyboardInput.length()){
                errors.add(new Exception("The rotor ["+cteRotor.getId()+"] is illegal,each signal in the ABC should be mapped in the rotor.You have "+amount+ " mappings,you should have "+keyboardInput.length() ));
            }
        }
    }
    private void isRotorsMappedIsLegal(){
        List<CTERotor> cteRotors=enigmaDescriptor.getCTEMachine().getCTERotors().getCTERotor();
        for (CTERotor cteRotor:cteRotors) {
           isRotorHasDoubleMapping(cteRotor);
        }
    }
    private void isNotchPositionLegal(){
        List<CTERotor> cteRotors=enigmaDescriptor.getCTEMachine().getCTERotors().getCTERotor();
        for (CTERotor cteRotor:cteRotors) {
            int notchPosition = cteRotor.getNotch();
            if(notchPosition<1){
                errors.add(new Exception("The rotor ["+cteRotor.getId()+"] is illegal,the notch position can not be less than 1" ));
            }
            if((notchPosition>keyboardInput.length())){
                errors.add(new Exception("The rotor ["+cteRotor.getId()+"] is illegal,the notch position can not be more than ABC length ["+keyboardInput.length() +"]" ));
            }
        }
    }
}