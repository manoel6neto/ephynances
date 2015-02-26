# encoding: UTF-8
class AddForeignKeys < ActiveRecord::Migration
  def change
    add_foreign_key "agreement_documents", "agreements", name: "agreement_documents_agreement_id_fk"
    add_foreign_key "agreement_installments", "agreements", name: "agreement_installments_agreement_id_fk"
    add_foreign_key "agreement_installments", "payments", name: "agreement_installments_payment_id_fk"
    add_foreign_key "agreements", "agreement_responsibles", name: "agreements_agreement_responsible_id_fk"
    add_foreign_key "agreements", "agreement_types", name: "agreements_agreement_type_id_fk"
    add_foreign_key "agreements", "city_organs", name: "agreements_city_organ_id_fk"
    add_foreign_key "agreements", "users", name: "agreements_user_id_fk"
    add_foreign_key "city_organs", "states", name: "city_organs_state_id_fk"
    add_foreign_key "payment_documents", "payments", name: "payment_documents_payment_id_fk"
    add_foreign_key "states", "regions", name: "states_region_id_fk"
    add_foreign_key "sub_agreement_installments", "agreement_installments", name: "sub_agreement_installments_agreement_installment_id_fk"
    add_foreign_key "sub_agreement_installments", "payments", name: "sub_agreement_installments_payment_id_fk"
    add_foreign_key "users", "city_organs", name: "users_city_organ_id_fk"
    add_foreign_key "users", "user_levels", name: "users_user_level_id_fk"
  end
end
