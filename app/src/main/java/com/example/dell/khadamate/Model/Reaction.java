package com.example.dell.khadamate.Model;

public class Reaction {

    private String ReactorFullName;
    private String ReactedFullName;
    private String Reaction;

    public Reaction(){

    }

    public Reaction(String reactorFullName, String reactedFullName, String reaction) {
        ReactorFullName = reactorFullName;
        ReactedFullName = reactedFullName;
        Reaction = reaction;
    }

    public String getReactorFullName() {
        return ReactorFullName;
    }

    public void setReactorFullName(String reactorFullName) {
        ReactorFullName = reactorFullName;
    }

    public String getReactedFullName() {
        return ReactedFullName;
    }

    public void setReactedFullName(String reactedFullName) {
        ReactedFullName = reactedFullName;
    }

    public String getReaction() {
        return Reaction;
    }

    public void setReaction(String reaction) {
        Reaction = reaction;
    }
}
