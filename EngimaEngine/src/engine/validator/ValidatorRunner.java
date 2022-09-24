package engine.validator;

import java.util.ArrayList;
import java.util.List;

public class ValidatorRunner {
      List<Validator> validators = new ArrayList<>();

    public ValidatorRunner(List<Validator> validators) {
        this.validators = validators;
    }

    public  List<Exception> run() {
        List<Exception> errors = new ArrayList<>();
        for (Validator validator : validators) {
            validator.validate();
            errors.addAll(validator.getListOfException());
        }
        return errors;
    }

}
