`{:description "This rule finds any template reaction record described in Reactome, as well as its Reactome id field.",
 :name "step_cb-add_reactome_template_reactions_to_ice",
 :reify ([?/template_reaction_record {:ns "http://ccp.ucdenver.edu/kabob/ice/", :ln (:sha-1 "Reactome template reaction record" ?/template_reaction), :prefix "R_"}]
          [?/this_xref_record {:ns "http://ccp.ucdenver.edu/kabob/ice/", :ln (:sha-1 ?/xref_record ?/template_reaction_record), :prefix "R_"}]
         [?/reactome_ice]
         ),
 :head ((?/record_set obo/BFO_0000051 ?/template_reaction_record)
        (?/template_reaction_record rdf/type ccp/IAO_EXT_0001554) ;; biochemical reaction record
        (?/template_reaction_record obo/BFO_0000051 ?/this_xref_record)
         (?/this_xref_record rdfs/subClassOf ?/xref_record)
         (?/this_xref_record rdf/type ccp/IAO_EXT_0001588) ;; xref field
        (?/xref_id_field rdf/type ?/reactome_ice)
        (?/reactome_ice rdfs/subClassOf ccp/IAO_EXT_0001643) ;; Reactome identifier
        (?/template_reaction ccp/ekws_temp_biopax_connector_relation ?/template_reaction_record)
        ),
 :body "#add_reactome_biochemical_reactions_to_ice.clj
PREFIX franzOption_chunkProcessingAllowed: <franz:yes>
PREFIX franzOption_clauseReorderer: <franz:identity>
PREFIX obo: <http://purl.obolibrary.org/obo/>
PREFIX ccp: <http://ccp.ucdenver.edu/obo/ext/>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
PREFIX bp: <http://www.biopax.org/release/biopax-level3.owl#>
PREFIX kice: <http://ccp.ucdenver.edu/kabob/ice/>
PREFIX kbio: <http://ccp.ucdenver.edu/kabob/bio/>
SELECT ?record_set ?template_reaction ?xref_record ?xref_id_field ?reactome_ice WHERE {
       ?template_reaction rdf:type bp:TemplateReaction .
       ?template_reaction bp:xref ?xref .
       ?xref ccp:ekws_temp_biopax_connector_relation ?xref_record .
       ?xref_record obo:BFO_0000051 ?xref_id_field .
       ?xref_id_field rdf:type ccp:IAO_EXT_0001520 .
       ?xref_id_field rdfs:label ?react_id .
       bind (uri (concat (str (\"http://ccp.ucdenver.edu/kabob/ice/REACTOME_\"), str (?react_id))) as ?reactome_ice) .
                          ?xref_record obo:BFO_0000051 ?xref_db_field .
                          ?xref_db_field rdf:type ccp:IAO_EXT_0001519 .
                          ?xref_db_field rdfs:label \"Reactome\"@en .

       {
        select ?record_set {
              ?record_set rdfs:label \"Reactome BioPAX record set, Homo sapiens\"@en .
                          ?record_set rdf:type ccp:IAO_EXT_0000012 .
                          ?record_set obo:IAO_0000142 ?download .
                          ?download rdfs:label \"http://www.reactome.org/pages/download-data/biopax/Homo_sapiens.owl\"@en .
              }
        }
  }",
  :options {:magic-prefixes [["franzOption_logQuery" "franz:yes"] ["franzOption_clauseReorderer" "franz:identity"]]}}




         

         
