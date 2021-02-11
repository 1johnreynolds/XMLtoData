import java.util.Scanner;

public class AnswerQuery {
    private static String chosenQuery;
    private static String queryA_AuthorName;
    private static String queryB_PaperName;
    private static String queryC_JournalName;
    private static String queryC_Year;
    private static String queryC_Issue;
    private static String queryD_ConferenceName;
    private static String queryD_Year;

    public String chosen() {
        return chosenQuery;
    }

    public String returnQueryA_AuthorName() {
        return queryA_AuthorName;
    }

    public String returnQueryB_PaperName() {
        return queryB_PaperName;
    }

    public String returnQueryC_JournalName() {
        return queryC_JournalName;
    }

    public String returnQueryC_Year() {
        return queryC_Year;
    }

    public String returnQueryC_Issue() {
        return queryC_Issue;
    }

    public String returnQueryD_ConferenceName() {
        return queryD_ConferenceName;
    }

    public String returnQueryD_Year() {
        return queryD_Year;
    }

    private static void answerQueryA() {
        chosenQuery = "a";

        Scanner qaAuthorName = new Scanner(System.in);
        System.out.print("Please tell me the author name: ");
        String authorName = qaAuthorName.nextLine();
        System.out.println("Given author name is: " + authorName);

        queryA_AuthorName = authorName;
//        returnQueryA_AuthorName();
    }

    private static void answerQueryB() {
        chosenQuery = "b";
        Scanner qbPaperName = new Scanner(System.in);
        System.out.print("Please tell me the paper name: ");
        String paperName = qbPaperName.nextLine();
        System.out.println("Given paper name is: " + paperName);

        queryB_PaperName = paperName;
    }

    private static void answerQueryC() {
        chosenQuery = "c";
        Scanner qcJournalName = new Scanner(System.in);
        System.out.print("Please tell me the journal name: ");
        String journalName = qcJournalName.nextLine();
        System.out.println("Given journal name is: " + journalName);

        Scanner qcYear = new Scanner(System.in);
        System.out.print("Please tell me the year: ");
        String year = qcYear.nextLine();
        System.out.println("Given year is: " + year);

        Scanner qcIssue = new Scanner(System.in);
        System.out.print("Please tell me the issue: ");
        String issue = qcIssue.nextLine();
        System.out.println("Given issue is: " + issue);

        queryC_JournalName = journalName;
        queryC_Year = year;
        queryC_Issue = issue;
    }

    private static void answerQueryD() {
        chosenQuery = "d";
        Scanner qdConferenceName = new Scanner(System.in);
        System.out.print("Please tell me the conference name: ");
        String conferenceName = qdConferenceName.nextLine();
        System.out.println("Given conference name is: " + conferenceName);

        Scanner qdYear = new Scanner(System.in);
        System.out.print("Please tell me the year: ");
        String year = qdYear.nextLine();
        System.out.println("Given year is: " + year);

        queryD_ConferenceName = conferenceName;
        queryD_Year = year;
    }

    private void answerQueryEx() {
        System.out.println("Query a: ");
        answerQueryA();
        System.out.println();

        System.out.println("Query b: ");
        answerQueryB();
        System.out.println();

        System.out.println("Query c: ");
        answerQueryC();
        System.out.println();

        System.out.println("Query d: ");
        answerQueryD();
        System.out.println();
    }
    private static void answerQueryQ() {
        chosenQuery = "q";
    }

    public AnswerQuery() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Which query do you want to choose?");
        System.out.println("Please choose: ");
        System.out.println("a. 1.3.1 Given author name A, list all of her co-authors\n" +
                "b. 1.3.2 Given a paper name, list its publication metadata\n" +
                "c. 1.3.3 Given a journal name, a year (volume) and an issue (number), find out the metadata of all the papers published in the book\n" +
                "d. 1.3.4 Given a conference name and a year, find out the metadata of all the papers published in the book\n" +
                "q. Quit the queries:\n");

        String chosen = sc.nextLine();
        if(!chosen.equals("q")){
            System.out.println("You have chosen query " + chosen);
        }
        if (chosen.equals("a")) {
            answerQueryA();
        } else if (chosen.equals("b")) {
            answerQueryB();
        } else if (chosen.equals("c")) {
            answerQueryC();
        } else if (chosen.equals("d")) {
            answerQueryD();
        } else if (chosen.equals("q")) {
            answerQueryQ();
            System.out.println("Quit the queries successfully!");
        } else {
            System.out.println("Sorry, this query is not applicable!Please check your Input!It is case and space sensitive.");
        }
    }
    public static void nextQuery() {
        Scanner sc = new Scanner(System.in);
        //System.out.println("---------------------------------------");
        System.out.println("Which query do you want to choose?");
        System.out.println("Please choose: ");
        System.out.println("a. 1.3.1 Given author name A, list all of her co-authors\n" +
                "b. 1.3.2 Given a paper name, list its publication metadata\n" +
                "c. 1.3.3 Given a journal name, a year (volume) and an issue (number), find out the metadata of all the papers published in the book\n" +
                "d. 1.3.4 Given a conference name and a year, find out the metadata of all the papers published in the book\n" +
                "q. Quit the queries:\n");

        String chosen = sc.nextLine();
        if(!chosen.equals("q")){
            System.out.println("You have chosen query " + chosen);
        }
        if (chosen.equals("a")) {
            answerQueryA();
        } else if (chosen.equals("b")) {
            answerQueryB();
        } else if (chosen.equals("c")) {
            answerQueryC();
        } else if (chosen.equals("d")) {
            answerQueryD();
        } else if (chosen.equals("q")) {
            answerQueryQ();
            System.out.println("Quit the queries successfully!");
        } else {
            System.out.println("Sorry, this query is not applicable!Please check your Input!It is case and space sensitive.");
        }
    }
}

