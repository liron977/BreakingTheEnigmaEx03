package engine.validator;

import java.util.List;

public interface Validator {

    public void validate();
    public List<Exception> getListOfException();

}