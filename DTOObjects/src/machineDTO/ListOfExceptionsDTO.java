package machineDTO;

import java.io.Serializable;
import java.util.List;

public class ListOfExceptionsDTO implements Serializable {
    private List<Exception> errors;
    public ListOfExceptionsDTO(List<Exception> errors){
        this.errors=errors;
    }
    public List<Exception> getListOfException(){
        return errors;
    }
    public void addException(Exception e){
        this.errors.add(e);
    }
}
