package housewifei;

public class ControllerFactory implements Factory{

    public Object create(String pattern){
        try{
            return Class.forName("housewifei."+pattern).getConstructor().newInstance();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

}
