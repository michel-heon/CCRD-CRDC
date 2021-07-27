package ca.uqam.vocabulary.crdc_ccrd;
/**
* <h1>CRDC CCRD Vocabulary builder</h1>
* <h2>Classification canadienne de la recherche et développement (CCRD) 2020 version 1.0</h2>
* <h2>Canadian Research and Development Classification (CRDC) 2020 Version 1.0</h2>
* 
* This program extracts data from CSV format to transform them into RDF/RDFS/OWL graphs
* 
* <p>
* <b>Note:</b> Giving proper comments in your program makes it more
* user friendly and it is assumed as a high quality code.
*
* @author  Michel Héon PhD
* @version 1.0
* @since   2021-07-26
*/
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.jena.lang.csv.CSV2RDF;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.OWL2;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

import ca.uqam.vocabulary.model.CRDC_CCRD;
import ca.uqam.vocabulary.model.CRDC_CCRD_CSV;




public class CrdcCcrd_VocabBuilder {

	private static final String CRDC_CCRD_CSV_DATA_TTL = "/crdc-ccrd-csv-data.ttl"; // all csv data
	private static final String W3C_CSV_VOCAB = "http://w3c/future-csv-vocab/";
	private static final String UQAM_CRDC_CCRD_CSV = "http://ca.uqam/crdc-ccrd/csv";
	private static final String CRDC_CCRD_VOCABULARY = "http://purl.org/uqam.ca/vocabulary/crdc-ccrd";
	private static final Property csvRowProp = ResourceFactory.createProperty(W3C_CSV_VOCAB+"row");
	private static final Property fromCsvFileProp = ResourceFactory.createProperty(UQAM_CRDC_CCRD_CSV+"#fromCsvFile");

	private static String ns;
	private static String baseUri;
	private static String SemanticBaseUri;
	private static String csv_ns=UQAM_CRDC_CCRD_CSV;
	String SPARQL_PREFIX = "" 
			+ "PREFIX crdc-ccrd-csv: <http://ca.uqam/crdc-ccrd/csv#> \n"
			+ "PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
			+ "PREFIX owl:   <http://www.w3.org/2002/07/owl#> \n"
			+ "PREFIX xsd:   <http://www.w3.org/2001/XMLSchema#> \n"
			+ "PREFIX rdfs:  <http://www.w3.org/2000/01/rdf-schema#> \n"
			+ "PREFIX csv-vocab: <http://w3c/future-csv-vocab/> \n"
			+ "PREFIX dc:    <http://purl.org/dc/elements/1.1/> \n";

	private Model inputCsvModel; // what is inside of CRDC_CCRD_CSV_DATA_TTL
	private Model outModel;
	private FileOutputStream fOut;
	private String inputFilePath;
	private String outputFilePath;
	private String graphFilePath;
	
	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		CrdcCcrd_VocabBuilder vocabBuilder = new CrdcCcrd_VocabBuilder();
		vocabBuilder.manageOptions(args);
		vocabBuilder.init();
		vocabBuilder.readAllCSV();
		for (int i = 1; i < 5; i++) {
			vocabBuilder.transformLevel(i);
		}
		for (int i = 1; i < 5; i++) {
			vocabBuilder.performElements(i);
		}

		vocabBuilder.write();

		//        for (String s: args) {
		//            System.err.println(s);
		//        }
		System.err.println("Done!");

	}
	
	private static void setPrefix(Model aModel) {
		aModel.setNsPrefix("crdc-ccrd", SemanticBaseUri+"#");
		aModel.setNsPrefix("", ns);
		aModel.setNsPrefix("crdc-ccrd-data", ns);
		aModel.setNsPrefix("owl", "http://www.w3.org/2002/07/owl#");
		aModel.setNsPrefix("terms", "http://purl.org/dc/terms/");
		aModel.setNsPrefix("xsd", "http://www.w3.org/2001/XMLSchema#");
		aModel.setNsPrefix("rdf", "http://www.w3.org/2000/01/rdf-schema#");
		aModel.setNsPrefix("skos2", "http://www.w3.org/2008/05/skos#");
		aModel.setNsPrefix("skos", "http://www.w3.org/2004/02/skos/core#");
		aModel.setNsPrefix("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
		aModel.setNsPrefix("crdc-ccrd-csv", csv_ns+"#");
		aModel.setNsPrefix("csv-vocab", W3C_CSV_VOCAB);
		aModel.setNsPrefix("rdfs","http://www.w3.org/2000/01/rdf-schema#");
	}

	private void init() {
		CSV2RDF.init();
		outModel = ModelFactory.createDefaultModel();
		SemanticBaseUri = CRDC_CCRD_VOCABULARY;
		baseUri = CRDC_CCRD_VOCABULARY+"/individual";
		ns = baseUri+"#";
		setPrefix(outModel);
		init_ontology();
		/*
		 * Create input model containing all csv's data 
		 */
		inputCsvModel = ModelFactory.createDefaultModel()
				.setNsPrefixes(outModel.getNsPrefixMap())
				.setNsPrefix("", csv_ns+"#");
	}


	private void init_ontology() {
		Resource ontoRes = ResourceFactory.createResource(baseUri);
		outModel.add(ontoRes, RDF.type, OWL.Ontology);
		outModel.add(ontoRes, OWL.imports, ResourceFactory.createResource(CRDC_CCRD.NS.replace("#", "")));
		outModel.add(ontoRes, RDFS.comment, ResourceFactory.
				createLangLiteral("La Classification canadienne de la recherche et du développement (CCRD) "
						+ "2020 a été élaborée conjointement par le Conseil de recherches en sciences "
						+ "humaines du Canada (CRSH), le Conseil de recherches en sciences naturelles "
						+ "et en génie du Canada (CRSNG), la Fondation canadienne pour l'innovation (FCI), "
						+ "les Instituts de recherche en santé du Canada (IRSC) et Statistique Canada qui en est le gardien. "
						+ "Cette classification-type conjointe, inspirée du modèle de Frascati 2015 "
						+ "de l'Organisation de Coopération et de Développement Économiques (OCDE), "
						+ "sera utilisée par les organismes subventionnaires fédéraux et Statistique "
						+ "Canada pour recueillir et diffuser des données liées à la recherche et au développement au Canada. "
						+ "La première version officielle de la CCRD est la version 1.0 de 2020 "
						+ "qui est composée de 3 éléments principaux: "
						+ "le type d'activité ou TDA (avec 3 catégories), "
						+ "le domaine de recherche ou DDR (avec 1663 domaines au niveau le plus bas) et l'objectif socioéconomique ou "
						+ "OSE (avec 85 groupes principaux au niveau le plus bas)",
						"fr-CA"));
		outModel.add(ontoRes, RDFS.comment, ResourceFactory.
				createLangLiteral("The Canadian Research and Development Classification (CRDC) "
						+ "was developed conjointly by the Social Sciences and Humanities Research Council of Canada (SSHRC), "
						+ "the Natural Sciences and Engineering Research Council of Canada (NSERC), "
						+ "the Canada Foundation for Innovation (CFI), the Canadian Institutes of Health Research (CIHR), "
						+ "and Statistics Canada which is the custodian. This shared standard classification, "
						+ "inspired by the Frascati Model 2015 of the Organisation for Economic Co-operation and Development (OECD), "
						+ "will be used by the federal granting agencies and Statistics Canada to collect and disseminate "
						+ "data related to research and development in Canada. The CRDC first official version "
						+ "is the 2020 version 1.0 and it is composed of 3 main pieces: the type of activity or"
						+ "TOA (with 3 categories), the field of research or FOR (with 1663 fields at "
						+ "the lowest level) and socioeconomic objective or SEO (with 85 main groups at the lowest level).",
						"en-CA"));
		outModel.add(ontoRes, RDFS.seeAlso, ResourceFactory.createResource("https://www.statcan.gc.ca/eng/subjects/standard/crdc/2020v1/index"));
		outModel.add(ontoRes, RDFS.seeAlso, ResourceFactory.createResource("https://www.statcan.gc.ca/fra/sujets/norme/ccrd/2020v1/indice"));
		outModel.add(ontoRes, RDFS.seeAlso, ResourceFactory.createResource("https://www.oecd.org/sti/inno/frascati-manual.htm"));
		outModel.add(ontoRes, OWL2.versionIRI, ResourceFactory.createResource("http://purl.org/vivo.uqam.ca/crdc_ccrd/ontology#v.1.0.0-20210721"));
		outModel.add(ontoRes, OWL.versionInfo, ResourceFactory.createPlainLiteral("Author: Michel Héon PhD : heon.michel@uqam.ca"));
		outModel.add(ontoRes, OWL.versionInfo, ResourceFactory.createPlainLiteral("Based on CRDC/CCRD 2020 Version 1.0 (This standard was approved as a recommended standard on May 26, 2020. )"));
		outModel.add(ontoRes, OWL.versionInfo, ResourceFactory.createPlainLiteral("Transformed in ontology by Université du Québec à Montréal (2021/07/21)"));
	}


	private void performElements(int levelNo) {
		String getAllLevel1QR_fr = SPARQL_PREFIX 
				+ "DESCRIBE  ?s \n"
				+ "WHERE { \n"
				+ "   ?s <"+CRDC_CCRD_CSV.Niveau.getURI() +"> \""+levelNo+"\"^^xsd:double .\n"
				+ "   ?s <"+CRDC_CCRD_CSV.Description_d_element_Francais.getURI() +"> ?value .\n"
				+ "}\n";

		String getAllLevel1QR_en = SPARQL_PREFIX 
				+ "DESCRIBE  ?s \n"
				+ "WHERE { \n"
				+ "   ?s <"+CRDC_CCRD_CSV.Level.getURI() +"> \""+levelNo+"\"^^xsd:double .\n"
				+ "   ?s <"+CRDC_CCRD_CSV.Element_Description_English.getURI() +"> ?value .\n"
				+ "}";
		Model describeLevel1Model = getModelFromDescribpQuery(getAllLevel1QR_fr, inputCsvModel);
		describeLevel1Model.add(getModelFromDescribpQuery(getAllLevel1QR_en, inputCsvModel));
		String getListOfCode = SPARQL_PREFIX 
				+ "SELECT  DISTINCT ?code \n"
				+ "WHERE { \n"
				+ "    ?s <" + CRDC_CCRD_CSV.Code.getURI()+ "> ?code "
				+ "}";
		ResultSet codesList = getModelFromSelectQuery(getListOfCode, describeLevel1Model);
		while (codesList.hasNext()) {
			QuerySolution qs = codesList.next();
			String code =qs.get("code").toString();
			String describeFromCode = SPARQL_PREFIX 
					+ "DESCRIBE  ?s "
					+ "WHERE { "
					+ "    ?s <" + CRDC_CCRD_CSV.Code.getURI()+ "> \"" +code+"\""
					+ "}";
			Model describeFromCodeModel = this.getModelFromDescribpQuery(describeFromCode, describeLevel1Model);
			String getListOfRow = SPARQL_PREFIX 
					+ "SELECT  DISTINCT ?row \n"
					+ "WHERE { \n"
					+ "    ?s <" + csvRowProp.getURI()+ "> ?row ."
					+ "}";
			ResultSet rowList = getModelFromSelectQuery(getListOfRow, describeFromCodeModel);
			while (rowList.hasNext()) {
				QuerySolution rowQS = rowList.next();
				String row =rowQS.get("row").asLiteral().getLexicalForm();
//				System.err.println("ELEMENT code="+ code + " Level=" +  levelNo +" row="+row);
				String describeFromRowQuery = SPARQL_PREFIX 
						+ "DESCRIBE  ?s \n"
						+ "WHERE { \n"
						+ "    ?s <" + csvRowProp.getURI()+ "> " +row+" \n"
						+ "}";
				Model instanceModel = getModelFromDescribpQuery(describeFromRowQuery, describeFromCodeModel);
				/*
				 * instance IRI encoding
				 */
				Resource instanceResource = ResourceFactory.createResource(ns+code+"."+row);
				ontoMapForInstanceCode(instanceResource, code, instanceModel);
				processElementLabel(instanceResource, instanceModel);
				if (levelNo==1) {
					classifyInstanceForDivision(instanceResource, code, instanceModel);
				} else if (levelNo==2) {
					Resource codeRes = ResourceFactory.createResource(ns+code);
					outModel.add(instanceResource,RDF.type, codeRes);
				} else if (levelNo==3) {
					Resource codeRes = ResourceFactory.createResource(ns+code);
					outModel.add(instanceResource,RDF.type, codeRes);
				} else if (levelNo==4) {
					Resource codeRes = ResourceFactory.createResource(ns+code);
					outModel.add(instanceResource,RDF.type, codeRes);
				}
			}
		}
	}


	private void processElementLabel(Resource instanceResource, Model instanceModel) {
			List<RDFNode> stmts_fr = instanceModel.listObjectsOfProperty(CRDC_CCRD_CSV.Description_d_element_Francais).toList();
			List<RDFNode> stmts_en = instanceModel.listObjectsOfProperty(CRDC_CCRD_CSV.Element_Description_English).toList();
			if(stmts_en!=null && !stmts_en.isEmpty()){
				String text = stmts_en.get(0).asLiteral().getLexicalForm();
				Literal literal = ResourceFactory.createLangLiteral(text, "en-CA");
				outModel.add(instanceResource, RDFS.label, literal);
			}
			if(stmts_fr!=null && !stmts_fr.isEmpty()){
				String text = stmts_fr.get(0).asLiteral().getLexicalForm();
				Literal literal = ResourceFactory.createLangLiteral(text, "fr-CA");
				outModel.add(instanceResource, RDFS.label, literal);
			}
	}

	private void ontoMapForInstanceCode(Resource instanceResource, String code, Model describeFromCodeModel) {
		outModel.add(instanceResource,CRDC_CCRD.hasID, instanceResource.getLocalName());
		outModel.add(instanceResource,RDF.type, OWL2.NamedIndividual);
		processClassTitle(instanceResource, describeFromCodeModel);
		processElementDescription(instanceResource, describeFromCodeModel);
		processElementTypeLabel(instanceResource, describeFromCodeModel);
		processHasLevel(instanceResource, describeFromCodeModel);	
		processSeeAlso(instanceResource, describeFromCodeModel);
	}

	private void classifyInstanceForDivision(Resource instanceResource, String code, Model describeFromCodeModel) {
		Resource codeRes = ResourceFactory.createResource(ns+code);
		outModel.add(instanceResource,RDF.type, codeRes);	
	}

	private void transformLevel(int levelNo) {
		String getAllLevel1QR_fr = SPARQL_PREFIX 
				+ "DESCRIBE  ?s \n"
				+ "WHERE { \n"
				+ "   ?s <"+CRDC_CCRD_CSV.Niveau.getURI() +"> \""+levelNo+"\"^^xsd:double .\n"
				+ "   ?s <"+CRDC_CCRD_CSV.Structure_hierarchique.getURI() +"> ?value .\n"
				+ "}\n";

		String getAllLevel1QR_en = SPARQL_PREFIX 
				+ "DESCRIBE  ?s \n"
				+ "WHERE { \n"
				+ "   ?s <"+CRDC_CCRD_CSV.Level.getURI() +"> \""+levelNo+"\"^^xsd:double .\n"
				+ "   ?s <"+CRDC_CCRD_CSV.Hierarchical_structure.getURI() +"> ?value .\n"
				+ "}";
		Model describeLevel1Model = getModelFromDescribpQuery(getAllLevel1QR_fr, inputCsvModel);
		describeLevel1Model.add(getModelFromDescribpQuery(getAllLevel1QR_en, inputCsvModel));
		List<Statement> stmts = describeLevel1Model.listStatements(null, CRDC_CCRD_CSV.Code, (RDFNode)null).toList();
		for (Statement stmt : stmts) {
			RDFNode obj = stmt.getObject();
			String code =obj.asLiteral().getLexicalForm();
			String describeFromCode = SPARQL_PREFIX 
					+ "DESCRIBE  ?s "
					+ "WHERE { "
					+ "    ?s <" + CRDC_CCRD_CSV.Code.getURI()+ "> \"" +code+"\""
					+ "}";
//			System.err.println("code  = "+code);
			Query describeFromCodeQuery = QueryFactory.create(describeFromCode);
			QueryExecution describeFromCodeQueryQexec = QueryExecutionFactory.create(describeFromCodeQuery, describeLevel1Model);
			Model describeFromCodeModel = describeFromCodeQueryQexec.execDescribe();
			Resource codeRes = ResourceFactory.createResource(ns+code);
			ontoMapForCode(codeRes, code, describeFromCodeModel);
			processClassLabel(codeRes, code, describeFromCodeModel);
			if (levelNo==1) {
				classifyEntityForDivision(code, describeFromCodeModel);
			} else if (levelNo==2) {
				classifyEntityForGroup(code, describeFromCodeModel);
			} else if (levelNo==3) {
				classifyEntityForClass(code, describeFromCodeModel);
			} else if (levelNo==4) {
				classifyEntityForSubClass(code, describeFromCodeModel);
			}
		}
	}

	private void processClassLabel(Resource codeRes, String code, Model describeFromCodeModel) {
		List<RDFNode> stmts_fr = describeFromCodeModel.listObjectsOfProperty(CRDC_CCRD_CSV.Titres_de_classes).toList();
		List<RDFNode> stmts_en = describeFromCodeModel.listObjectsOfProperty(CRDC_CCRD_CSV.Class_title).toList();
		String literal_fr = stmts_fr.get(0).asLiteral().getLexicalForm();
		String literal_en = stmts_en.get(0).asLiteral().getLexicalForm();
		Literal classTitle_en = ResourceFactory.createLangLiteral(literal_en, "en-CA");
		Literal classTitle_fr = ResourceFactory.createLangLiteral(literal_fr, "fr-CA");
		outModel.add(codeRes, RDFS.label, classTitle_en);
		outModel.add(codeRes, RDFS.label, classTitle_fr);		
	}

	private Model getModelFromDescribpQuery(String queryString, Model modelToQuery) {
		Query query = QueryFactory.create(queryString);
		QueryExecution qexec = QueryExecutionFactory.create(query, modelToQuery);
		Model queryModel = qexec.execDescribe();
		return queryModel;
	}
	private void classifyEntityForDivision(String code, Model describeFromCodeModel) {
		Resource codeRes = ResourceFactory.createResource(ns+code);
		String getfromCsvFile = SPARQL_PREFIX 
				+ "SELECT DISTINCT ?fn "
				+ "WHERE { "
				+ "    ?s <" + CRDC_CCRD_CSV.fromCsvFile+ "> ?fn . "
				+ "}";
		ResultSet getfromCsvFileRS = getModelFromSelectQuery(getfromCsvFile, describeFromCodeModel);
		String csvFile = getfromCsvFileRS.next().getLiteral("fn").getLexicalForm();
		if (csvFile.contains("seo-ose")){
			outModel.add(codeRes, RDFS.subClassOf, CRDC_CCRD.SEO_OSE_Entity);	
		} else if (csvFile.contains("for-ddr")){
			outModel.add(codeRes, RDFS.subClassOf, CRDC_CCRD.FOR_DDR_Entity);
		} else if (csvFile.contains("toa-tda")){
			outModel.add(codeRes, RDFS.subClassOf, CRDC_CCRD.TOA_TDA_Entity);
		} 		
		outModel.add(codeRes,RDF.type, CRDC_CCRD.CRDC_CCRD_Division);
	}
	private void classifyEntityForSubClass(String code, Model describeFromCodeModel) {
		Resource codeRes = ResourceFactory.createResource(ns+code);
		outModel.add(codeRes,RDF.type, CRDC_CCRD.CRDC_CCRD_SubClass);	
		String upperCode = code.substring(0, code.length() - 2);
		Resource upperCodeRes = ResourceFactory.createResource(ns+upperCode);
		outModel.add(codeRes,RDFS.subClassOf, upperCodeRes);

	}

	private void classifyEntityForClass(String code, Model describeFromCodeModel) {
		Resource codeRes = ResourceFactory.createResource(ns+code);
		outModel.add(codeRes,RDF.type, CRDC_CCRD.CRDC_CCRD_Class);

		String upperCode = code.substring(0, code.length() - 2);
		Resource upperCodeRes = ResourceFactory.createResource(ns+upperCode);
		outModel.add(codeRes,RDFS.subClassOf, upperCodeRes);

	}

	private void classifyEntityForGroup(String code, Model describeFromCodeModel) {
		Resource codeRes = ResourceFactory.createResource(ns+code);
		outModel.add(codeRes,RDF.type, CRDC_CCRD.CRDC_CCRD_Group);
		if (code.contains("RDF20") || code.contains("RDF21")){
			String upperCode = "RDF20-21";
			Resource upperCodeRes = ResourceFactory.createResource(ns+upperCode);
			outModel.add(codeRes,RDFS.subClassOf, upperCodeRes);
		} else if (code.contains("RDF")){
			String upperCode = code.substring(0, code.length() - 1);
			Resource upperCodeRes = ResourceFactory.createResource(ns+upperCode);
			outModel.add(codeRes,RDFS.subClassOf, upperCodeRes);
		} else {
			String upperCode = code.substring(0, code.length() - 2);
			Resource upperCodeRes = ResourceFactory.createResource(ns+upperCode);
			outModel.add(codeRes,RDFS.subClassOf, upperCodeRes);
		}
	}

	private void ontoMapForCode(Resource codeRes, String code, Model describeFromCodeModel) {
		outModel.add(codeRes,CRDC_CCRD.hasID, code);
		outModel.add(codeRes,RDF.type, OWL.Class);
		processClassTitle(codeRes, describeFromCodeModel);
		processClasstDefenition(codeRes, describeFromCodeModel);
		processElementTypeLabel(codeRes, describeFromCodeModel);
		processHasLevel(codeRes, describeFromCodeModel);
		processHashasHierarchicalStructure(codeRes, describeFromCodeModel);
		processSeeAlso(codeRes, describeFromCodeModel);
	}

	@SuppressWarnings("static-access")
	private void processSeeAlso(Resource codeRes, Model describeFromCodeModel) {
		List<RDFNode> stmts = describeFromCodeModel.listObjectsOfProperty(CRDC_CCRD_CSV.fromCsvFile).toList();
		List<RDFNode> stmts_row = describeFromCodeModel.listObjectsOfProperty(this.csvRowProp).toList();
		for (Iterator iterator = stmts.iterator(); iterator.hasNext();) {
			RDFNode rdfNode = (RDFNode) iterator.next();
			String fileName = rdfNode.asLiteral().getLexicalForm();
			if(fileName==null ){
				return;
			}
			if (fileName.contains("-fra")){
				String literalString="";
				if(stmts!=null && !stmts.isEmpty()){
					literalString += " fileName: "+fileName;
				}
				if(stmts_row!=null && !stmts_row.isEmpty()){
					String row = stmts_row.get(0).asLiteral().getLexicalForm();
					literalString += " row: "+row;
				}
				Literal literal = ResourceFactory.createPlainLiteral(literalString);
				outModel.add(codeRes, RDFS.seeAlso, literal);
			} else if (fileName.contains("-eng")){
				String literalString="";
				if(stmts!=null && !stmts.isEmpty()){
					literalString += " fileName: "+fileName;
				}
				if(stmts_row!=null && !stmts_row.isEmpty()){
					String row = stmts_row.get(0).asLiteral().getLexicalForm();
					literalString += " row: "+row;
				}
				Literal literal = ResourceFactory.createPlainLiteral(literalString);
				outModel.add(codeRes, RDFS.seeAlso, literal);
			}
		}
	}

	private void processHashasHierarchicalStructure(Resource codeRes, Model describeFromCodeModel) {
		List<RDFNode> stmts_fr = describeFromCodeModel.listObjectsOfProperty(CRDC_CCRD_CSV.Structure_hierarchique).toList();
		List<RDFNode> stmts_en = describeFromCodeModel.listObjectsOfProperty(CRDC_CCRD_CSV.Hierarchical_structure).toList();
		if(stmts_en!=null && !stmts_en.isEmpty()){
			String text = stmts_en.get(0).asLiteral().getLexicalForm();
			Literal literal = ResourceFactory.createLangLiteral(text, "en-CA");
			outModel.add(codeRes, CRDC_CCRD.hasHierarchicalStructure, literal);
		}
		if(stmts_fr!=null && !stmts_fr.isEmpty()){
			String text = stmts_fr.get(0).asLiteral().getLexicalForm();
			Literal literal = ResourceFactory.createLangLiteral(text, "fr-CA");
			outModel.add(codeRes, CRDC_CCRD.hasHierarchicalStructure, literal);
		}
	}

	private void processHasLevel(Resource codeRes, Model describeFromCodeModel) {
		List<RDFNode> stmts= describeFromCodeModel.listObjectsOfProperty(CRDC_CCRD_CSV.Level).toList();
		if(stmts!=null && !stmts.isEmpty()){
			Literal literal = stmts.get(0).asLiteral();
			outModel.add(codeRes, CRDC_CCRD.hasLevel, literal);
		}
	}

	private void processElementTypeLabel(Resource codeRes, Model describeFromCodeModel) {
		List<RDFNode> stmts_fr = describeFromCodeModel.listObjectsOfProperty(CRDC_CCRD_CSV.Nom_du_type_d_element_Francais).toList();
		List<RDFNode> stmts_en = describeFromCodeModel.listObjectsOfProperty(CRDC_CCRD_CSV.Element_Type_Label_English).toList();
		if(stmts_en!=null && !stmts_en.isEmpty()){
			String text = stmts_en.get(0).asLiteral().getLexicalForm();
			Literal literal = ResourceFactory.createLangLiteral(text, "en-CA");
			outModel.add(codeRes, CRDC_CCRD.elementTypeLabel, literal);
		}
		if(stmts_fr!=null && !stmts_fr.isEmpty()){
			String text = stmts_fr.get(0).asLiteral().getLexicalForm();
			Literal literal = ResourceFactory.createLangLiteral(text, "fr-CA");
			outModel.add(codeRes, CRDC_CCRD.elementTypeLabel, literal);
		}
	}

	private void processElementDescription(Resource codeRes, Model describeFromCodeModel) {
		List<RDFNode> stmts_fr = describeFromCodeModel.listObjectsOfProperty(CRDC_CCRD_CSV.Description_d_element_Francais).toList();
		List<RDFNode> stmts_en = describeFromCodeModel.listObjectsOfProperty(CRDC_CCRD_CSV.Element_Description_English).toList();
		if(stmts_en!=null && !stmts_en.isEmpty()){
			String text = stmts_en.get(0).asLiteral().getLexicalForm();
			Literal literal = ResourceFactory.createLangLiteral(text, "en-CA");
			outModel.add(codeRes, CRDC_CCRD.elementDescription, literal);
		}
		if(stmts_fr!=null && !stmts_fr.isEmpty()){
			String text = stmts_fr.get(0).asLiteral().getLexicalForm();
			Literal literal = ResourceFactory.createLangLiteral(text, "fr-CA");
			outModel.add(codeRes, CRDC_CCRD.elementDescription, literal);
		}
	}

	private void processClasstDefenition(Resource codeRes, Model describeFromCodeModel) {
		List<RDFNode> stmts_fr = describeFromCodeModel.listObjectsOfProperty(CRDC_CCRD_CSV.Definitions_de_la_classe).toList();
		List<RDFNode> stmts_en = describeFromCodeModel.listObjectsOfProperty(CRDC_CCRD_CSV.Class_definition).toList();
		if(stmts_en!=null && !stmts_en.isEmpty()){
			String text = stmts_en.get(0).asLiteral().getLexicalForm();
			Literal literal = ResourceFactory.createLangLiteral(text, "en-CA");
			outModel.add(codeRes, CRDC_CCRD.classDefinition, literal);
		}
		if(stmts_fr!=null && !stmts_fr.isEmpty()){
			String text = stmts_fr.get(0).asLiteral().getLexicalForm();
			Literal literal = ResourceFactory.createLangLiteral(text, "fr-CA");
			outModel.add(codeRes, CRDC_CCRD.classDefinition, literal);
		}
	}

	private void processClassTitle(Resource codeRes, Model describeFromCodeModel) {
		List<RDFNode> stmts_fr = describeFromCodeModel.listObjectsOfProperty(CRDC_CCRD_CSV.Titres_de_classes).toList();
		List<RDFNode> stmts_en = describeFromCodeModel.listObjectsOfProperty(CRDC_CCRD_CSV.Class_title).toList();
		String literal_fr = stmts_fr.get(0).asLiteral().getLexicalForm();
		String literal_en = stmts_en.get(0).asLiteral().getLexicalForm();
		Literal classTitle_en = ResourceFactory.createLangLiteral(literal_en, "en-CA");
		Literal classTitle_fr = ResourceFactory.createLangLiteral(literal_fr, "fr-CA");
		outModel.add(codeRes, CRDC_CCRD.classTitle, classTitle_en);
		outModel.add(codeRes, CRDC_CCRD.classTitle, classTitle_fr);
	}

	private ResultSet getModelFromSelectQuery(String queryString, Model queryOnModel) {
		Query query = QueryFactory.create(queryString);
		QueryExecution queryExec = QueryExecutionFactory.create(query, queryOnModel);
		ResultSet modelResult = queryExec.execSelect();
		return modelResult;
	}

	private void readAllCSV() throws FileNotFoundException {
		File dir = new File(inputFilePath);
		File [] files = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".csv");
			}
		});
		try {

			for (File filename : files) {
				// Print the names of files and directories
//				System.err.println(filename);
				Model inModel = load(inputFilePath, filename.getName());
				inputCsvModel.add(inModel);
			}
			OutputStream outStream = new FileOutputStream(outputFilePath+CRDC_CCRD_CSV_DATA_TTL);
			inputCsvModel.write(outStream, "ttl",UQAM_CRDC_CCRD_CSV);

		} catch (java.lang.NullPointerException e) {
			System.err.println("("+inputFilePath+") does not exist!");
			System.exit(-1);
		}
	}

	private void manageOptions(String[] args) {
		Options options = new Options();

		Option input = new Option("i", "input", true, "input csv files path");
		input.setRequired(true);
		options.addOption(input);

		Option output = new Option("o", "output", true, "output path for dataset");
		output.setRequired(true);
		options.addOption(output);
		Option graph = new Option("g", "graph", true, "output graph filename (stdout if it is not specified)");
		graph.setRequired(false);
		options.addOption(graph);

		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd = null;//not a good practice, it serves it purpose 

		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			System.err.println(e.getMessage());
			formatter.printHelp("utility-name", options);

			System.exit(1);
		}

		inputFilePath = cmd.getOptionValue("input").replaceFirst(" /", "/");
		outputFilePath = cmd.getOptionValue("output").replaceFirst(" /", "/");
		graphFilePath = cmd.getOptionValue("graph");

//		System.err.println("("+inputFilePath+")");
//		System.err.println("("+outputFilePath+")");

	}

	private Model load(String pathname, String filename) throws FileNotFoundException {
		InputStream stream = new FileInputStream(pathname+"/"+filename);
		String basename = filename.replace(".csv", "");
		Literal basenameLiteral = ResourceFactory.createStringLiteral(basename);
		OutputStream outStream = new FileOutputStream(outputFilePath+"/"+basename+".ttl");
		Model CSV_model = ModelFactory.createDefaultModel()
				.setNsPrefixes(outModel.getNsPrefixMap())
				.setNsPrefix("", csv_ns+"#");
		CSV_model.read(stream, csv_ns, "csv");
		List<Statement> stmts = CSV_model.listStatements(null,csvRowProp, (RDFNode)null).toList();
		for (Statement stmt : stmts) {
			Statement lStmt = CSV_model.createStatement(stmt.getSubject(), fromCsvFileProp, basenameLiteral);
			CSV_model.add(lStmt);
		}
		CSV_model.write(outStream, "ttl");
		return CSV_model;
		//		System.exit(-1);
	}

	private void write() throws IOException {
		if (graphFilePath == null ){
			outModel.write(System.out,"ttl",baseUri);
		}
		else {
			fOut = new FileOutputStream(graphFilePath);
			outModel.write(fOut,"ttl",baseUri );
			fOut.close();		
		}

	}


}
