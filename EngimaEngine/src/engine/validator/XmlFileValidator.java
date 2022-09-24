package engine.validator;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XmlFileValidator implements Validator{

    private List<Exception> errors;
    private String filePath;

    public XmlFileValidator(String filePath){
        this.filePath=filePath;
        errors=new ArrayList<>();
    }

    private void isPathIsValid() {
            int len = filePath.length();
            if (len < 4) {
                errors.add(new Exception("File full name is too short!"));
            }
            else if (!filePath.endsWith(".xml")) {
                errors.add(new Exception("This is not a full path of a xml file!"));
            }
            else {
                File file = new File(filePath);
                if (!file.exists()) {
                    errors.add(new Exception(("can not find the file [" + filePath + "]")));
                }
            }
    }
    @Override
    public void validate() {
        isPathIsValid();
    }

    @Override
    public List<Exception> getListOfException() {
        return errors;
    }
}
