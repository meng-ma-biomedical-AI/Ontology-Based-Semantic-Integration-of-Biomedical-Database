`{:description "This rule finds any protein reference record described in Reactome with a UniProt Isoform (not UniProt) ID.",
 :name "add_reactome_protein_references_with_uniprot_isoform_ids_to_ice",
 :reify ([?/prot_ref_record {:ns "http://ccp.ucdenver.edu/kabob/ice/", :ln (:sha-1 "Reactome protein reference record" ?/prot_ref), :prefix "R_"}]
         [?/this_xref_record {:ns "http://ccp.ucdenver.edu/kabob/ice/", :ln (:sha-1 ?/xref_record ?/prot_ref_record), :prefix "R_"}]
         ),
  :head ((?/prot_ref_record rdf/type ccp/IAO_EXT_0001551) ;; protein reference
         (?/prot_ref_record obo/BFO_0000051 ?/this_xref_record)
         (?/this_xref_record rdfs/subClassOf ?/xref_record)
         (?/this_xref_record rdf/type ccp/IAO_EXT_0001588) ;; xref field
         (?/xref_id_field rdf/type ccp/IAO_EXT_0001599) ;; reactome uniprot isoform id field
         (?/xref_id_field rdf/type ?/uniprot_ice)
         (?/prot_ref ccp/ekws_temp_biopax_connector_relation ?/prot_ref_record)),
  :body "#add_reactome_protein_references_with_uniprot_isoform_ids_to_ice.clj
PREFIX franzOption_chunkProcessingAllowed: <franz:yes>
PREFIX franzOption_clauseReorderer: <franz:identity>
PREFIX obo: <http://purl.obolibrary.org/obo/>
PREFIX ccp: <http://ccp.ucdenver.edu/obo/ext/>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX bp: <http://www.biopax.org/release/biopax-level3.owl#>
PREFIX kice: <http://ccp.ucdenver.edu/kabob/ice/>
PREFIX kbio: <http://ccp.ucdenver.edu/kabob/bio/>
SELECT DISTINCT ?prot_ref ?xref_record ?xref_id_field ?uniprot_ice WHERE {
?prot_ref rdf:type bp:ProteinReference .
?prot_ref bp:xref ?xref .
?xref ccp:ekws_temp_biopax_connector_relation ?xref_record .
?xref_record obo:BFO_0000051 ?xref_id_field .
?xref_id_field rdf:type ccp:IAO_EXT_0001520 .
?xref_id_field rdfs:label ?uniprot_id .
?xref_record obo:BFO_0000051 ?xref_db_field .
?xref_db_field rdf:type ccp:IAO_EXT_0001519 .
?xref_db_field rdfs:label \"UniProt Isoform\"@en .
bind (uri (concat (str (\"http://ccp.ucdenver.edu/kabob/ice/UNIPROT_\"), str (?uniprot_id))) as ?uniprot_ice) . 
}",
  :options {:magic-prefixes [["franzOption_logQuery" "franz:yes"] ["franzOption_clauseReorderer" "franz:identity"]]}}

