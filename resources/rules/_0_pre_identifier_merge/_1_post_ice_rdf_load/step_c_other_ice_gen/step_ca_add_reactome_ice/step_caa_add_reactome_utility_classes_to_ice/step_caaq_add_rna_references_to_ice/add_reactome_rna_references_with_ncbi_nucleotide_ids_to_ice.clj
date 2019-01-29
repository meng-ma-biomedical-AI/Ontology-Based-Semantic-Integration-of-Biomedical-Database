`{:description "This rule finds any rna reference record described in Reactome with an NCBI nucleotide ID via an HGNC id. It may miss names that don't match the HGNC id.",
 :name "add_reactome_rna_references_with_ncbi_nucleotide_ids_to_ice",
 :reify ([?/rna_ref_record {:ns "http://ccp.ucdenver.edu/kabob/ice/", :ln (:sha-1 "Reactome rna reference record" ?/rna_ref), :prefix "R_"}]
         [?/this_xref_record {:ns "http://ccp.ucdenver.edu/kabob/ice/", :ln (:sha-1 ?/xref_record ?/rna_ref_record), :prefix "R_"}]
         ),
  :head ((?/rna_ref_record rdf/type ccp/IAO_EXT_0001561) ;; rna reference
         (?/rna_ref_record obo/BFO_0000051 ?/this_xref_record)
         (?/this_xref_record rdfs/subClassOf ?/xref_record) 
         (?/this_xref_record rdf/type ccp/IAO_EXT_0001588) ;; xref field
         (?/xref_id_field rdf/type ?/refseq_id)
         (?/xref_id_field rdf/type ccp/IAO_EXT_0001969) ;; reactome RNA id field value
         (?/rna_ref ccp/ekws_temp_biopax_connector_relation ?/rna_ref_record)),
  :body "#add_reactome_rna_references_with_ncbi_nucleotide_ids_to_ice.clj
PREFIX franzOption_chunkProcessingAllowed: <franz:yes>
PREFIX franzOption_clauseReorderer: <franz:identity>
PREFIX obo: <http://purl.obolibrary.org/obo/>
PREFIX ccp: <http://ccp.ucdenver.edu/obo/ext/>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX bp: <http://www.biopax.org/release/biopax-level3.owl#>
PREFIX kice: <http://ccp.ucdenver.edu/kabob/ice/>
PREFIX kbio: <http://ccp.ucdenver.edu/kabob/bio/>
SELECT DISTINCT ?rna_ref ?xref_record ?xref_id_field ?refseq_id WHERE {
?rna_ref rdf:type bp:RnaReference .
?rna_ref bp:name ?react_name .
?rna_ref bp:xref ?xref .
?xref ccp:ekws_temp_biopax_connector_relation ?xref_record .
?xref_record obo:BFO_0000051 ?xref_id_field .
?xref_id_field rdf:type ccp:IAO_EXT_0001520 .
?xref_id_field rdfs:label ?ncbi_nucleotide_id .
?xref_record obo:BFO_0000051 ?xref_db_field .
?xref_db_field rdf:type ccp:IAO_EXT_0001519 .
?xref_db_field rdfs:label \"NCBI Nucleotide\"@en .
# Although this id is labeled 'NCBI Nucleotide' it is in fact a REFSEQ identifier
bind (uri (concat (str (\"http://ccp.ucdenver.edu/kabob/ice/REFSEQ_\"), str (?ncbi_nucleotide_id))) as ?refseq_id) .
}",
  :options {:magic-prefixes [["franzOption_logQuery" "franz:yes"] ["franzOption_clauseReorderer" "franz:identity"]]}}

