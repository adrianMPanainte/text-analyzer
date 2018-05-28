import java.util.Set;
import java.util.TreeSet;

public class Concept implements Comparable<Concept>{
    private String name;
    private String className;
    private ConceptClass conceptClass;
    private String conceptSubclass;
    private String url;
    private Set<String> characteristics = new TreeSet<>();
    private Boolean foundInDB;
    private int accuracy = 1;

    public String getClassName() {
        return className;
    }
    public void setClassName(String className) {
        this.className = className;
    }
    public String getConceptSubclass() {
        return conceptSubclass;
    }
    public void setConceptSubclass(String conceptSubclass) {
        this.conceptSubclass = conceptSubclass;
    }
    public Set<String> getCharacteristics() {
        return characteristics;
    }
    public void addCharacteristics(String characteristics) {
        this.characteristics.add(characteristics);
    }
    public void setCharacteristics(Set<String> characteristics) {
        this.characteristics = characteristics;
    }
    public String getName() {
        return name;
    }
    public ConceptClass getConceptClass() {
        return conceptClass;
    }
    public String getUrl() {
        return url;
    }
    public int getAccuracy() { return accuracy; }
    public void setName(String name) {
        this.name = name;
    }
    public void setConceptClass(ConceptClass conceptClass) {
        this.conceptClass = conceptClass;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public void setFoundInDB(Boolean foundInDB) {
        this.foundInDB = foundInDB;
    }
    public Boolean getFoundInDB() {
        return foundInDB;
    }

    Concept() { this.foundInDB = false; }


    @Override
    public int compareTo(Concept obj) {
        try {
            if (this == obj) return 0;
            int code = this.name.compareTo(obj.name);
            if (code != 0)
                return code;

            return 0;
        } catch (NullPointerException e) {
            return 1;
        }
    }


    @Override
    public String toString() {
        return "Concept{" +
                "name='" + name + '\'' +
                ", conceptClass=" + className +
                ", conceptSubclass='" + conceptSubclass + '\'' +
                ", url='" + url + '\'' +
                ", characteristics=" + characteristics +
                ", foundInDB=" + foundInDB +
                ", accuracy=" + accuracy +
                '}';
    }
}
