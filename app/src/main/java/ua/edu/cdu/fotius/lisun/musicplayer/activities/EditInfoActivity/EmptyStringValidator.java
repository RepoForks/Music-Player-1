package ua.edu.cdu.fotius.lisun.musicplayer.activities.EditInfoActivity;

public class EmptyStringValidator extends BaseValidator{

    protected EmptyStringValidator(String invalidityMessage) {
        super(invalidityMessage);
    }

    @Override
    public boolean validate(Object forValidation) {
        String s = (String) forValidation;
        if((s == null) || (s.isEmpty())) {
            return false;
        }
        return true;
    }
}
