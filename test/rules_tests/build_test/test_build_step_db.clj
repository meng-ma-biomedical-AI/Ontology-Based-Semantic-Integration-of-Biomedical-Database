(ns rules-tests.build_test.test_build_step_db
  (use clojure.test
       kr.sesame.kb
       kr.sesame.sparql
       kr.sesame.rdf
       )
  (:require [kabob.build.run-rules :refer [query-variables]]
            [kr.core.forward-rule :refer [add-reify-fns]]
            [kr.core.sparql :refer [sparql-select-query query sparql-query ask]]
            [kr.core.rdf :refer [register-namespaces synch-ns-mappings add! *graph*]]
            [kr.core.kb :refer [kb open close]]
            [kabob.core.namespace :refer [*namespaces*]]
            [kabob.core.rule :refer [kabob-load-rules-from-classpath]]
            [kabob.build.output-kb :refer [output-kb]]
            [clojure.pprint :refer [pprint]]
            [rules-tests.build-test.ccp-ext-ontology :refer [ccp-ext-ontology-triples]]
            [rules-tests.build-test.test-build-util :refer [concepts object-properties other-identifiers-mentioned-in-records ice-identifiers
                                                            initial-plus-ice-triples run-build-rule run-build-rules
                                                            test-kb build-rules-step-a build-rules-step-b
                                                            build-rules-step-ca build-rules-step-cb build-rules-step-cc build-rules-step-cd
                                                            build-rules-step-da build-rules-step-db build-rules-step-dc]]))



(def base-kb (let [source-kb (test-kb initial-plus-ice-triples)]
               (binding [*graph* "http://ccp-extension.owl"]
                 (dorun (map (partial add! source-kb) ccp-ext-ontology-triples)))
               (run-build-rules source-kb build-rules-step-a)
               (run-build-rules source-kb build-rules-step-b)
               (run-build-rules source-kb build-rules-step-ca)
               (run-build-rules source-kb build-rules-step-cb)
               (run-build-rules source-kb build-rules-step-cc)
               (run-build-rules source-kb build-rules-step-cd)
               source-kb))

;;;
;;; rules/pre_identifier_merge/post_ice_rdf_load/step_d_ice_id_exact_match/drugbank_drug_identifier_exact_match
;;; Test the generation of skos:exactMatch links to DrugBank identifiers based on the DrugBank data source
;;;
;;; TODO: add DrugBank ICE triples to the input_data to test this rule.
(deftest step-db-drugbank-identifier-exact-match
  (let [source-kb base-kb
        target-kb (test-kb '())]
    (run-build-rules source-kb build-rules-step-da)
    (run-build-rule source-kb target-kb build-rules-step-db 0)

    ;; there are 4 metadata triples for each rule run, so 4 metadata triples and 0 rule output triples expected here
    (is (= 4 (count (query target-kb '((?/s ?/p ?/o))))))))

;;;
;;; rules/pre_identifier_merge/post_ice_rdf_load/step_d_ice_id_exact_match/hgnc_gene_symbol_entrez_identifier_exact_match
;;; Test the generation of skos:exactMatch links between HGNC symbols and NCBI gene identifiers based on the HGNC data source
;;;
(deftest step-db-hgnc-symbol-to-ncbi-gene-identifier-exact-match
  (let [source-kb base-kb
        target-kb (test-kb '())]
    (run-build-rules source-kb build-rules-step-da)
    (run-build-rule source-kb target-kb build-rules-step-db 8)

    (is (ask target-kb '((kice/HGNC_TGFBR2 skos/exactMatch kice/NCBI_GENE_7048))))
    (is (ask target-kb '((kice/HGNC_A1BG skos/exactMatch kice/NCBI_GENE_1))))

    ;; there are 4 metadata triples for each rule run, so 4 metadata triples and 0 rule output triples expected here
    (is (= 6 (count (query target-kb '((?/s ?/p ?/o))))))))


;;;
;;; rules/pre_identifier_merge/post_ice_rdf_load/step_d_ice_id_exact_match/hgnc_gene_symbol_hgnc_identifier_exact_match
;;; Test the generation of skos:exactMatch links between HGNC symbols and NCBI gene identifiers based on the HGNC data source
;;;
(deftest step-db-hgnc-symbol-to-hgnc-gene-identifier-exact-match
  (let [source-kb base-kb
        target-kb (test-kb '())]
    (run-build-rules source-kb build-rules-step-da)
    (run-build-rule source-kb target-kb build-rules-step-db 9)

    (is (ask target-kb '((kice/HGNC_TGFBR2 skos/exactMatch kice/HGNC_11773))))
    (is (ask target-kb '((kice/HGNC_A1BG skos/exactMatch kice/HGNC_5))))

    ;; there are 4 metadata triples for each rule run, so 4 metadata triples and 0 rule output triples expected here
    (is (= 6 (count (query target-kb '((?/s ?/p ?/o))))))
    ))

;;;;
;;;; rules/pre_identifier_merge/post_ice_rdf_load/step_d_ice_id_exact_match/hgnc_gene_symbol_refseq_identifier_exact_match
;;;; Test the generation of skos:exactMatch links between HGNC symbols and NCBI gene identifiers based on the HGNC data source
;;;;
;(deftest step-d-hgnc-symbol-to-refseq-identifier-exact-match
;  (let [source-kb (test-kb initial-plus-ice-triples)
;        target-kb (test-kb '())]
;    (run-build-rules source-kb build-rules-step-a)
;    (run-build-rules source-kb build-rules-step-b)
;(run-build-rules source-kb build-rules-step-ca)
;(run-build-rules source-kb build-rules-step-cb)
;(run-build-rules source-kb build-rules-step-cc)
;(run-build-rules source-kb build-rules-step-da)
;    (run-build-rule source-kb target-kb build-rules-step-db 3)
;
;    ;;(is (ask target-kb '((kice/HGNC_TGFBR2 skos/exactMatch kice/HGNC_11773))))
;
;    ;; there are 4 metadata triples for each rule run, so 4 metadata triples and 0 rule output triples expected here
;    (is (= 4 (count (query target-kb '((?/s ?/p ?/o))))))
;    ))





;;;;
;;;; rules/pre_identifier_merge/post_ice_rdf_load/step_d_ice_id_exact_match/hgnc_gene_symbol_supplied_refseq_identifier_exact_match
;;;; Test the generation of skos:exactMatch links between HGNC symbols and NCBI gene identifiers based on the HGNC data source
;;;;
;(deftest step-d-hgnc-symbol-to-supplied-refseq-identifier-exact-match
;  (let [source-kb (test-kb initial-plus-ice-triples)
;        target-kb (test-kb '())]
;    (run-build-rules source-kb build-rules-step-a)
;    (run-build-rules source-kb build-rules-step-b)
;(run-build-rules source-kb build-rules-step-ca)
;(run-build-rules source-kb build-rules-step-cb)
;(run-build-rules source-kb build-rules-step-cc)
;(run-build-rules source-kb build-rules-step-da)
;    (run-build-rule source-kb target-kb build-rules-step-db 6)
;
;    ;; there are 4 metadata triples for each rule run, so 4 metadata triples and 0 rule output triples expected here
;    (is (= 4 (count (query target-kb '((?/s ?/p ?/o))))))
;    ))


;;;;
;;;; rules/pre_identifier_merge/post_ice_rdf_load/step_d_ice_id_exact_match/mgi_gene_entrez_identifier_exact_match
;;;; Test the generation of skos:exactMatch links between HGNC symbols and NCBI gene identifiers based on the HGNC data source
;;;;
;;;; TODO: convert this rule to use sparql-string
;(deftest step-d-mgi-to-ncbi-gene-identifier-exact-match
;  (let [source-kb (test-kb initial-plus-ice-triples)
;        target-kb (test-kb '())]
;    ;(run-build-rules source-kb build-rules-step-a 3)
;    ;(run-build-rules source-kb build-rules-step-b 2)
;    ;(run-build-rules source-kb build-rules-step-c 3)
;    ;(run-build-rule source-kb target-kb build-rules-step-db 10)
;    ;
;    ;;; there are 4 metadata triples for each rule run, so 4 metadata triples and 0 rule output triples expected here
;    ;(is (= 4 (count (query target-kb '((?/s ?/p ?/o))))))
;    ))


;;;;
;;;; rules/pre_identifier_merge/post_ice_rdf_load/step_d_ice_id_exact_match/ncbi_gene_identifier_exact_match
;;;; Test the generation of skos:exactMatch links between HGNC symbols and NCBI gene identifiers based on the HGNC data source
;;;;
;(deftest step-d-ncbi-gene-to-other-identifier-exact-match
;  (let [source-kb (test-kb initial-plus-ice-triples)
;        target-kb (test-kb '())]
;    (run-build-rules source-kb build-rules-step-a)
;    (run-build-rules source-kb build-rules-step-b)
;(run-build-rules source-kb build-rules-step-ca)
;(run-build-rules source-kb build-rules-step-cb)
;(run-build-rules source-kb build-rules-step-cc)
;(run-build-rules source-kb build-rules-step-da)
;    (run-build-rule source-kb target-kb build-rules-step-db 11)
;
;    (is (ask target-kb '((kice/NCBI_GENE_7048 skos/exactMatch kice/HGNC_11773))))
;
;    ;; there are 4 metadata triples for each rule run, so 4 metadata triples and 1 rule output triples expected here
;    (is (= 5 (count (query target-kb '((?/s ?/p ?/o))))))
;
;
;    ;(let [log-kb (output-kb "/tmp/triples.nt")
;    ;      src-kb (test-kb initial-plus-ice-triples)]
;    ;
;    ;  ;; add sample triples to the log kb
;    ;  (run-build-rules src-kb build-rules-step-a)
;    ;  (run-build-rules src-kb build-rules-step-b)
;    ;  (run-build-rules src-kb build-rules-step-c)
;    ;  (run-build-rule src-kb log-kb build-rules-step-db 11)
;    ;  (close log-kb))
;
;    ))



;;;
;;; rules/pre_identifier_merge/post_ice_rdf_load/step_d_ice_id_exact_match/pharmgkb_drug_identifier_exact_match
;;; Test the generation of skos:exactMatch links between HGNC symbols and NCBI gene identifiers based on the HGNC data source
;;;
;;; TODO: add test triples to input_data to test this rule
(deftest step-db-pharmgkb-drug-to-other-identifier-exact-match
  (let [source-kb base-kb
        target-kb (test-kb '())]
    (run-build-rules source-kb build-rules-step-da)
    (run-build-rule source-kb target-kb build-rules-step-db 10)

    ;; there are 4 metadata triples for each rule run, so 4 metadata triples and 0 rule output triples expected here
    (is (= 4 (count (query target-kb '((?/s ?/p ?/o))))))
    ))




;;;
;;; rules/pre_identifier_merge/post_ice_rdf_load/step_d_ice_id_exact_match/pharmgkb_gene_entrez_identifier_exact_match
;;; Test the generation of skos:exactMatch links between HGNC symbols and NCBI gene identifiers based on the HGNC data source
;;;
;;; TODO: add test triples to input_data to test this rule
(deftest step-db-pharmgkb-gene-to-ncbi-gene-identifier-exact-match
  (let [source-kb base-kb
        target-kb (test-kb '())]
    (run-build-rules source-kb build-rules-step-da)
    (run-build-rule source-kb target-kb build-rules-step-db 11)

    ;; there are 4 metadata triples for each rule run, so 4 metadata triples and 0 rule output triples expected here
    (is (= 4 (count (query target-kb '((?/s ?/p ?/o))))))
    ))



;;;
;;; rules/pre_identifier_merge/post_ice_rdf_load/step_d_ice_id_exact_match/pro_protein_identifier_exact_match
;;; Test the generation of skos:exactMatch links between HGNC symbols and NCBI gene identifiers based on the HGNC data source
;;;
;;; TODO: add 'exact' info to the record in order to test this rule
(deftest step-db-pr-to-other-identifier-exact-match
  (let [source-kb base-kb
        target-kb (test-kb '())]
    (run-build-rules source-kb build-rules-step-da)
    (run-build-rule source-kb target-kb build-rules-step-db 12)

    (is (ask target-kb '((kice/PR_P37173 skos/exactMatch kice/UNIPROT_P37173))))
    (is (ask target-kb '((kice/PR_P37173-1 skos/exactMatch kice/UNIPROT_P37173-1))))

    ;; there are 4 metadata triples for each rule run, so 4 metadata triples and 2 rule output triples expected here
    (is (= 6 (count (query target-kb '((?/s ?/p ?/o))))))
    ))



;
;;;;
;;;; rules/pre_identifier_merge/post_ice_rdf_load/step_d_ice_id_exact_match/rgd_gene_entrez_identifier_exact_match
;;;; Test the generation of skos:exactMatch links between HGNC symbols and NCBI gene identifiers based on the HGNC data source
;;;;
;;;; TODO: convert rule to use sparql-string
;(deftest step-d-rgd-to-ncbi-gene-identifier-exact-match
;  (let [source-kb (test-kb initial-plus-ice-triples)
;        target-kb (test-kb '())]
;    ;(run-build-rules source-kb build-rules-step-a 3)
;    ;(run-build-rules source-kb build-rules-step-b 2)
;    ;(run-build-rules source-kb build-rules-step-c 3)
;    ;(run-build-rule source-kb target-kb build-rules-step-db 16)
;    ;
;    ;;; there are 4 metadata triples for each rule run, so 4 metadata triples and 0 rule output triples expected here
;    ;(is (= 4 (count (query target-kb '((?/s ?/p ?/o))))))
;    ))



(deftest step-db-uniprot-isoform-to-refseq-identifier-exact-match
  (let [source-kb base-kb
        target-kb (test-kb '())]
    (run-build-rules source-kb build-rules-step-da)
    (run-build-rule source-kb target-kb build-rules-step-db 16)

    (is (ask target-kb '((kice/UNIPROT_P37173-2 skos/exactMatch kice/REFSEQ_NP_001020018))))
    (is (ask target-kb '((kice/UNIPROT_P37173-1 skos/exactMatch kice/REFSEQ_NP_003233))))

    ;; there are 4 metadata triples for each rule run, so 4 metadata triples and 2 rule output triples expected here
    (is (= 6 (count (query target-kb '((?/s ?/p ?/o))))))))


(deftest step-db-uniprot-primary-accession-to-entry-name-exact-match
  (let [source-kb base-kb
        target-kb (test-kb '())]
    (run-build-rules source-kb build-rules-step-da)
    (run-build-rule source-kb target-kb build-rules-step-db 17)

    (is (ask target-kb '((kice/UNIPROT_P37173 skos/exactMatch kice/UNIPROTENTRYNAME_TGFR2_HUMAN))))
    (is (ask target-kb '((kice/UNIPROT_P62258 skos/exactMatch kice/UNIPROTENTRYNAME_1433E_HUMAN))))

    ;; there are 4 metadata triples for each rule run, so 4 metadata triples and 2 rule output triples expected here
    (is (= 6 (count (query target-kb '((?/s ?/p ?/o))))))





    ;(prn (str "--------------------------------"))
    ;(doall (map #(prn (str %)) (sparql-query source-kb
    ;                                         "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
    ;                                          prefix ccp: <http://ccp.ucdenver.edu/obo/ext/>
    ;                                          prefix obo: <http://purl.obolibrary.org/obo/>
    ;                                          prefix owl: <http://www.w3.org/2002/07/owl#>
    ;                                          prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>
    ;                                          select * {
    ;                                            ?db_ref_record rdf:type ccp:IAO_EXT_0000955 . # CCP:uniprot_database_reference_record
    ;                                            ?db_ref_record rdf:type ccp:IAO_EXT_0000940 . # CCP:uniprot_kb_record_database_reference_field_value
    ;                                            ?db_ref_record obo:BFO_0000051 ?id_field_value .
    ;                                            ?id_field_value rdf:type ?refseq_id .
    ;                                            ?refseq_id rdfs:subClassOf ccp:IAO_EXT_0001638 . # ccp:refseq_protein_identifier
    ;                                            ?db_ref_record obo:BFO_0000051 ?molecule_record .
    ;                                            ?molecule_record rdf:type ccp:IAO_EXT_0001712 . # CCP:uniprot_database_reference_record___molecule_field_value
    ;                                            ?molecule_record rdf:type ccp:IAO_EXT_0000961 . # CCP:uniprot_molecule_type_record
    ;                                            ?molecule_record obo:BFO_0000051 ?id_field .
    ;                                            ?id_field rdf:type ccp:IAO_EXT_0001147 . # CCP:uniprot_molecule_type_record___identifier_field_value
    ;                                            ?id_field rdf:type ?uniprot_id .
    ;                                            ?uniprot_id rdfs:subClassOf ccp:IAO_EXT_0001495 . # CCP:uniprot_identifier
    ;      }"
    ;                                         )))
    ;
    ;    (prn (str "--------------------------------"))


    ;;; The code fragment below is useful for debugging as it writes
    ;;; triples to a local file.
    ;(let [log-kb (output-kb "/tmp/triples.nt")
    ;      src-kb (test-kb initial-plus-ice-triples)]
    ;
    ;  ;; add sample triples to the log kb
    ;  (dorun (map (partial add! log-kb) initial-plus-ice-triples))
    ;
    ;  (run-build-rule src-kb log-kb build-rules-step-a 0)
    ;  (run-build-rule src-kb log-kb build-rules-step-a 1)
    ;  (run-build-rule src-kb log-kb build-rules-step-a 2)
    ;  (run-build-rule src-kb log-kb build-rules-step-a 3)
    ;  (run-build-rules src-kb build-rules-step-a 3)
    ;
    ;  (run-build-rule src-kb log-kb build-rules-step-b 0)
    ;  (run-build-rule src-kb log-kb build-rules-step-b 1)
    ;  (run-build-rule src-kb log-kb build-rules-step-b 2)
    ;  (run-build-rules src-kb build-rules-step-b 2)
    ;
    ;  (run-build-rule src-kb log-kb build-rules-step-c 0)
    ;  (run-build-rule src-kb log-kb build-rules-step-c 1)
    ;  (run-build-rule src-kb log-kb build-rules-step-c 2)
    ;  (run-build-rule src-kb log-kb build-rules-step-c 3)
    ;  (run-build-rules src-kb build-rules-step-c 3)
    ;
    ;  ;
    ;  ;(run-build-rule src-kb log-kb 0)
    ;  ;(run-build-rule src-kb src-kb 0)
    ;  ;
    ;  ;(run-build-rule src-kb log-kb 1)
    ;  ;(run-build-rule src-kb src-kb 1)
    ;  ;(run-build-rule source-kb log-kb build-rules-step-a 3)
    ;  (close log-kb))

    ))






