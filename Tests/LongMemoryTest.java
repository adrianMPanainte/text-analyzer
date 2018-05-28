package com.company;

import org.junit.Test;

import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.*;
import static org.junit.Assert.assertFalse;

public class LongMemoryTest {

    LongMemory test=new LongMemory();
    LongMemory test2=new LongMemory("brannkin","root","");
    LongMemory test3=new LongMemory("brmjnjain");


    @Test //should fail
    public void testValidDB_constructor2() {
        test3.search("concepts", "brain");
    }

    @Test //should fail
    public void testValidDB_constructor3() {
        test2.search("concepts", "brain");

    }

    @Test //if the input is null or the concept_key is not in the DB
    public void testSearchNotFoundInConcepts() {
        Concept result;
        result=(Concept)test.search("concepts", "something that doesn''t exist");
        assertFalse(result.getFoundInDB());

    }


    @Test //if the input is null or the concept_key is not in the DB
    public void testSearchNotFoundInClassConcepts() {
        ConceptClass result;
        result=(ConceptClass)test.search("concepts_class", "something that doesn''t exist");
        assertNull(result.getSubclass());

    }



    @Test //should be runned before test for duplicates
    public void testIfAddAdds() {
        String nameClass="dog", subclass="retriever-gun dog";
        Set<String> keywords=new TreeSet<>();
        keywords.add("Labrador Retriever");
        ConceptClass conceptClass=new ConceptClass(nameClass);
        Set<String> characteristics=new TreeSet<>();
        characteristics.add("cute");
        conceptClass.setSubclass(subclass);
        conceptClass.setKeywords(keywords);
        conceptClass.setCharacteristics(characteristics);


        String name=null, url="https://en.wikipedia.org/wiki/Labrador_Retriever";
        Concept concept=new Concept();
        concept.setName(name);
        concept.setConceptClass(conceptClass);
        concept.setConceptSubclass(subclass);
        concept.setCharacteristics(characteristics);
        concept.setUrl(url);

        test.add(concept, "concepts");
        test.add(conceptClass,"concepts_class");
        Concept result;
        result=(Concept)test.search("concepts", concept.getName());
        assertTrue(result.getFoundInDB());

    }

    @Test
    public void testIfAddDuplicate() {
        String nameClass="dog", subclass="retriever-gun dog";
        Set<String> keywords=new TreeSet<>();
        keywords.add("Labrador Retriever");
        ConceptClass conceptClass=new ConceptClass(nameClass);
        Set<String> characteristics=new TreeSet<>();
        characteristics.add("cute");
        conceptClass.setSubclass(subclass);
        conceptClass.setKeywords(keywords);
        conceptClass.setCharacteristics(characteristics);


        String name="animal", url="https://en.wikipedia.org/wiki/Labrador_Retriever";
        Concept concept=new Concept();
        concept.setName(name);
        concept.setConceptClass(conceptClass);
        concept.setConceptSubclass(subclass);
        concept.setCharacteristics(characteristics);
        concept.setUrl(url);

        test.add(concept, "concepts");
        test.add(conceptClass,"concepts_class");
    }

    @Test
    public void testAddNullInConcepts() {

        String nameClass=null, subclass=null;
        Set<String> keywords=new TreeSet<>();
        ConceptClass conceptClass=new ConceptClass(nameClass);
        Set<String> characteristics=new TreeSet<>();
        conceptClass.setSubclass(subclass);
        conceptClass.setKeywords(keywords);
        conceptClass.setCharacteristics(characteristics);


        String name=null, url="https://en.wikipedia.org/wiki/Labrador_Retriever";
        Concept concept=new Concept();
        concept.setName(name);
        concept.setConceptClass(conceptClass);
        concept.setConceptSubclass(subclass);
        concept.setCharacteristics(characteristics);
        concept.setUrl(url);

        test.add(concept, "concepts");
        test.add(conceptClass,"concepts_class");
        Object result;
        result=test.search("concepts", concept.getName());
        assertFalse(concept.getFoundInDB());

    }

    @Test
    public void testAddEmptyParameters() {
        String nameClass="", subclass="";
        Set<String> keywords=new TreeSet<>();
        keywords.add("krr");
        ConceptClass conceptClass=new ConceptClass(nameClass);
        conceptClass.setSubclass(subclass);
        conceptClass.setKeywords(keywords);
        conceptClass.setCharacteristics();


        String name="", url="";
        Concept concept=new Concept();
        concept.setName(name);
        concept.setConceptClass(conceptClass);
        concept.setConceptClass(conceptClass);
        concept.setConceptSubclass(subclass);
        concept.setCharacteristics();
        concept.setUrl(url);

        test.add(concept, "concepts");
        test.add(conceptClass,"concepts_class");
        Concept result;
        result=(Concept)test.search("concepts", concept.getName());
        assertTrue(result.getFoundInDB()); //should work only with null actually

    }



    @Test //if an existing concept_key is found
    public void testSearchFoundInConcepts() {
        Concept result;
        result=(Concept)test.search("concepts", "labrador");
        assertTrue(result.getFoundInDB());
    }


    @Test //if the input is null or the concept_key is not in the DB
    public void testSearchFoundInClassConcepts() {
        ConceptClass result;
        result=(ConceptClass)test.search("concepts_class", "dog");
        assertNotNull(result.getSubclass());

    }
}
