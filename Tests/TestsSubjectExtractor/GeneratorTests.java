abstract class GenerateTests implements Runnable {

    public static String Generare(String nume_fara_spatii,String nume, int i){

        nume.replaceAll(" ","");


        return " @Test\n" +
                "    public void "+ nume_fara_spatii+i+"() {\n" +
                "         StanfordCoreNLP pipeline;\n" +
                "        CoreDocument document;\n" +
                "        Properties props = new Properties();\n" +
                "        props.setProperty(\"annotators\", \"tokenize, ssplit, pos,parse\");\n" +
                "        pipeline = new StanfordCoreNLP(props);\n" +
                "        String text =\""+nume+" are awesome.\";\n" +
                "        document = new CoreDocument(text);\n" +
                "        pipeline.annotate(document);\n" +
                "        SubjectExtractor detectiv= new SubjectExtractor();\n" +
                "        detectiv.createList(document);\n" +
                "        Set<String> subiecti_trecuti = detectiv.getPassingSubjects();\n" +
                "        System.out.println(subiecti_trecuti);\n" +
                "        Set<String> verificare = new TreeSet<>();\n" +
                "        verificare.add(\""+nume+"\");\n" +
                "        assertEquals(verificare,subiecti_trecuti);\n" +
                "    }";

    }

    public static void main(String[] args){
        String[] nume={"dog","gorilla"};
        for(int i=0;i<nume.length;i++){
            System.out.println(Generare(nume[i].replaceAll("\\s+", "").replaceAll("'","").replaceAll("-",""),nume[i],i));
        }
    }

}