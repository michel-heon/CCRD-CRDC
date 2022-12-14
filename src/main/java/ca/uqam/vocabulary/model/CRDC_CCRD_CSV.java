/* CVS $Id: $ */
package ca.uqam.vocabulary.model; 
import org.apache.jena.rdf.model.*;
 
/**
 * Vocabulary definitions from src/main/resources/01-datasets/crdc-ccrd-csv-data.ttl 
 * @author Auto-generated by schemagen on 25 juill. 2021 15:19
 * 
 */
public class CRDC_CCRD_CSV {
    /** <p>The RDF model that holds the vocabulary terms</p> */
    private static final Model M_MODEL = ModelFactory.createDefaultModel();
    public static final String UQAM_CRDC_CCRD_CSV = "http://vivo.uqam.ca/vocabulary/crdc-ccrd/csv";
    /** <p>The namespace of the vocabulary as a string</p> */
    public static final String NS = UQAM_CRDC_CCRD_CSV +"#";
    
    /** <p>The namespace of the vocabulary as a string</p>
     * @return namespace as String
     * @see #NS */
    public static String getURI() {return NS;}
    
    /** <p>The namespace of the vocabulary as a resource</p> */
    public static final Resource NAMESPACE = M_MODEL.createResource( NS );
    
    public static final Property Class_definition = M_MODEL.createProperty( NS + "Class_definition" );
    
    public static final Property Class_title = M_MODEL.createProperty( NS + "Class_title" );
    
    public static final Property Code = M_MODEL.createProperty( NS + "Code" );
    
    public static final Property Definitions_de_la_classe = M_MODEL.createProperty( NS + "Definitions_de_la_classe" );
    
    public static final Property Description_d_element_Francais = M_MODEL.createProperty( NS + "Description_d_element_Francais" );
    
    public static final Property Element_Description_English = M_MODEL.createProperty( NS + "Element_Description_English" );
    
    public static final Property Element_Type_Label_English = M_MODEL.createProperty( NS + "Element_Type_Label_English" );
    
    public static final Property Hierarchical_structure = M_MODEL.createProperty( NS + "Hierarchical_structure" );
    
    public static final Property Nom_du_type_d_element_Francais = M_MODEL.createProperty( NS + "Nom_du_type_d_element_Francais" );
    
    public static final Property Structure_hierarchique = M_MODEL.createProperty( NS + "Structure_hierarchique" );
    
    public static final Property Titres_de_classes = M_MODEL.createProperty( NS + "Titres_de_classes" );
    
    public static final Property fromCsvFile = M_MODEL.createProperty( NS + "fromCsvFile" );
    
    public static final Property Level = M_MODEL.createProperty( NS + "﻿Level" );
    
    public static final Property Niveau = M_MODEL.createProperty( NS + "﻿Niveau" );
    
}
