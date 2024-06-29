package app.added.kannauj.Models;

public class ClassModel {

    String id,className,classSymbol,classSection;

    public ClassModel(String id, String className, String classSymbol, String classSection) {
        this.id = id;
        this.className = className;
        this.classSymbol = classSymbol;
        this.classSection = classSection;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassSymbol() {
        return classSymbol;
    }

    public void setClassSymbol(String classSymbol) {
        this.classSymbol = classSymbol;
    }

    public String getClassSection() {
        return classSection;
    }

    public void setClassSection(String classSection) {
        this.classSection = classSection;
    }
}
