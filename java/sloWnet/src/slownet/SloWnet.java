/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slownet;

import java.io.File;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.xml.sax.SAXException;

/**
 *
 * @author Klemen
 */
public class SloWnet {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            if (args.length == 0) {
                System.out.println("Napaka: Ni parametrov. Potreben je vsaj en parameter (pot do vhodne datoteke).");
                System.exit(1);
            }

            File inFile = new File(args[0]);
            File outFile = new File(inFile.getAbsolutePath().replace(".txt", "_expanded.txt"));

            if (inFile.exists() && inFile.getName().endsWith(".txt")) {
                if (outFile.exists()) {
                    Scanner in = new Scanner(System.in);
                    String response = "";
                    while (!(response.equals("y") || response.equals("n"))) {
                        System.out.print("Izhodna datoteka (\"" + outFile.getName() + "\") že obstaja in bo prepisana. Vseeno nadaljujem? (y/n)?: ");
                        response = in.nextLine();
                    }
                    in.close();
                    if (response.equals("n")) {
                        System.out.println("Napaka: Program prekinjen.");
                        System.exit(1);
                    }
                }
            } else {
                System.out.println("Napaka: Neveljavna vhodna datoteka..");
                System.exit(1);
            }

            boolean samoPreverjene = false;
            if (args.length > 1 && args[1].equals("true")) {
                samoPreverjene = true;
            }

            File f = new File("data\\slownet-2015-05-07.xml");

            int vsehBesed = 0;
            int preverjenih = 0;
            int nepreverjenih = 0;

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.parse(f);

            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();
            XPathExpression expr = xpath.compile("/WN/SYNSET/SYNONYM[@lang=\"sl\"]");
            NodeList nl = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

            ArrayList<ArrayList<String>> sopomenke = new ArrayList();

            for (int i = 0; i < nl.getLength(); i++) {
                Element synonim = (Element) nl.item(i);

                NodeList literals = synonim.getElementsByTagName("LITERAL");

                ArrayList<String> al = new ArrayList();

                for (int j = 0; j < literals.getLength(); j++) {
                    vsehBesed++;
                    Element literal = (Element) literals.item(j);
                    NamedNodeMap attributes = literal.getAttributes();

                    if (attributes.getLength() != 1) {
                        System.out.println("Opozorilo: LITERAL z napačnim številom atributov.");
                    }

                    String lnote = attributes.item(0).toString();

                    if (lnote.equals("lnote=\"manual\"")) {
                        preverjenih++;
                        al.add(literal.getTextContent());
                    } else if (lnote.equals("lnote=\"auto\"")) {
                        nepreverjenih++;
                        if (!samoPreverjene) {
                            al.add(literal.getTextContent());
                        }
                    } else {
                        System.out.println("Opozorilo: LITERAL z neznanim \"lnote\" atributom.");
                    }
                }
                if (al.size() > 1) {
                    sopomenke.add(al);
                }
            }

            int skupin = 0;
            int besed = 0;

            for (ArrayList<String> x : sopomenke) {
                for (String s : x) {
                    //System.out.print("\"" + s + "\" ");
                    besed++;
                }
                skupin++;
                //System.out.println("");
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(inFile), "UTF-8"));
            PrintWriter pw = new PrintWriter(outFile);

            ArrayList<String> inBesede = new ArrayList();
            ArrayList<String> outBesede = new ArrayList();

            int vhodnihBesed = 0;
            int vhodnihDuplikatov = 0;
            int najdenih = 0;
            int nenajdenih = 0;
            int dodanihBesed = 0;

            String b;
            boolean prvic = true;

            while ((b = in.readLine()) != null) {
                if (prvic && b.charAt(0) == 65279) {
                    b = b.substring(1);
                    prvic = false;
                }

                if (inBesede.contains(b)) {
                    vhodnihDuplikatov++;
                } else {
                    inBesede.add(b);
                    vhodnihBesed++;

                    outBesede.add(b);
                    pw.println(b);
                }
            }

            System.out.println("\nVseh besed: " + vsehBesed);
            System.out.println("Ročno preverjenih: " + preverjenih);
            System.out.println("Nepreverjenih: " + nepreverjenih);
            System.out.println("Parameter \"samo ročno preverjene besede\": " + samoPreverjene);
            System.out.println("Sopomenskih skupin (glede na parameter): " + skupin);
            System.out.println("Sopomenk (glede na parameter): " + besed);

            System.out.print("\nVhodnih besed: " + vhodnihBesed);
            if (vhodnihDuplikatov == 0) {
                System.out.println("");
            } else {
                System.out.println(" (odstranjenih je bilo " + vhodnihDuplikatov + " duplikatov)");
            }
            System.out.print("Besede brez sopomenk: ");

            for (String s : inBesede) {
                boolean imaSopomenke = false;
                for (ArrayList<String> skupina : sopomenke) {
                    if (skupina.contains(s)) {
                        imaSopomenke = true;
                        for (String sopomenka : skupina) {
                            if (!outBesede.contains(sopomenka)) {
                                outBesede.add(sopomenka);
                                pw.println(sopomenka);
                                dodanihBesed++;
                            }
                        }
                    }
                }

                if (imaSopomenke) {
                    najdenih++;
                } else {
                    if (nenajdenih == 0) {
                        System.out.print("\"" + s + "\"");
                    } else {
                        System.out.print(", \"" + s + "\"");
                    }
                    nenajdenih++;
                }
            }

            pw.close();
            in.close();

            if (nenajdenih == 0) {
                System.out.println("/");
            } else {
                System.out.println("");
            }
            System.out.println(nenajdenih + " besed je brez sopomenk, iz preostalih " + najdenih + " besed pa je bilo najdenih še " + dodanihBesed + " dodatnih sopomenk.");
            System.out.println("V datoteko \"" + outFile.getName() + "\" je bilo zapisanih skupaj " + (vhodnihBesed + dodanihBesed) + " besed.");

        } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException ex) {
            Logger.getLogger(SloWnet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}