package machineDTO;

import java.util.List;

public class ListOfErrorsDTO {
    private List<String> errors;
    public ListOfErrorsDTO(List<String> errors){
        this.errors=errors;
    }
    public List<String> getListOfException(){
        return errors;
    }
}
