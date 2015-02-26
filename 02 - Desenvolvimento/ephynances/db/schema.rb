# encoding: UTF-8
# This file is auto-generated from the current state of the database. Instead
# of editing this file, please use the migrations feature of Active Record to
# incrementally modify your database, and then regenerate this schema definition.
#
# Note that this schema.rb definition is the authoritative source for your
# database schema. If you need to create the application database on another
# system, you should be using db:schema:load, not running all the migrations
# from scratch. The latter is a flawed and unsustainable approach (the more migrations
# you'll amass, the slower it'll run and the greater likelihood for issues).
#
# It's strongly recommended that you check this file into your version control system.

ActiveRecord::Schema.define(version: 20150226125940) do

  create_table "agreement_documents", force: :cascade do |t|
    t.integer  "agreement_id", limit: 4
    t.string   "name",         limit: 255
    t.string   "extension",    limit: 255
    t.text     "description",  limit: 65535
    t.integer  "size",         limit: 4
    t.binary   "file",         limit: 65535
    t.datetime "created_at",                 null: false
    t.datetime "updated_at",                 null: false
  end

  add_index "agreement_documents", ["agreement_id"], name: "index_agreement_documents_on_agreement_id", using: :btree

  create_table "agreement_installments", force: :cascade do |t|
    t.integer  "agreement_id",     limit: 4
    t.integer  "payment_id",       limit: 4
    t.decimal  "value",                      precision: 10, scale: 2
    t.boolean  "status",           limit: 1,                          default: false
    t.date     "paymentDate"
    t.date     "dueDate"
    t.date     "confirmationDate"
    t.datetime "created_at",                                                          null: false
    t.datetime "updated_at",                                                          null: false
  end

  add_index "agreement_installments", ["agreement_id"], name: "index_agreement_installments_on_agreement_id", using: :btree
  add_index "agreement_installments", ["payment_id"], name: "index_agreement_installments_on_payment_id", using: :btree

  create_table "agreement_responsibles", force: :cascade do |t|
    t.string   "name",       limit: 255
    t.string   "email",      limit: 255
    t.string   "phone",      limit: 255
    t.string   "cellPhone",  limit: 255
    t.string   "cpf",        limit: 255
    t.datetime "created_at",             null: false
    t.datetime "updated_at",             null: false
  end

  create_table "agreement_types", force: :cascade do |t|
    t.string   "type",       limit: 255
    t.datetime "created_at",             null: false
    t.datetime "updated_at",             null: false
  end

  add_index "agreement_types", ["type"], name: "index_agreement_types_on_type", unique: true, using: :btree

  create_table "agreements", force: :cascade do |t|
    t.integer  "city_organ_id",             limit: 4
    t.integer  "user_id",                   limit: 4
    t.integer  "agreement_responsible_id",  limit: 4
    t.integer  "agreement_type_id",         limit: 4
    t.string   "contactEmail",              limit: 255
    t.string   "contactPhone",              limit: 255
    t.decimal  "totalPrice",                            precision: 10, scale: 2
    t.string   "contractorAgreementNumber", limit: 255
    t.string   "physisAgreementNumber",     limit: 255
    t.date     "expiryDate"
    t.boolean  "status",                    limit: 1,                            default: false
    t.integer  "cnpjAmount",                limit: 4,                            default: 0
    t.string   "documentNumber",            limit: 255
    t.datetime "created_at",                                                                     null: false
    t.datetime "updated_at",                                                                     null: false
  end

  add_index "agreements", ["agreement_responsible_id"], name: "index_agreements_on_agreement_responsible_id", using: :btree
  add_index "agreements", ["agreement_type_id"], name: "index_agreements_on_agreement_type_id", using: :btree
  add_index "agreements", ["city_organ_id"], name: "index_agreements_on_city_organ_id", using: :btree
  add_index "agreements", ["contractorAgreementNumber"], name: "index_agreements_on_contractorAgreementNumber", unique: true, using: :btree
  add_index "agreements", ["physisAgreementNumber"], name: "index_agreements_on_physisAgreementNumber", unique: true, using: :btree
  add_index "agreements", ["user_id"], name: "index_agreements_on_user_id", using: :btree

  create_table "city_organs", force: :cascade do |t|
    t.integer  "state_id",   limit: 4
    t.string   "cnpj",       limit: 255
    t.string   "cityName",   limit: 255
    t.string   "organName",  limit: 255
    t.datetime "created_at",             null: false
    t.datetime "updated_at",             null: false
  end

  add_index "city_organs", ["cnpj"], name: "index_city_organs_on_cnpj", unique: true, using: :btree
  add_index "city_organs", ["state_id"], name: "index_city_organs_on_state_id", using: :btree

  create_table "payment_documents", force: :cascade do |t|
    t.integer  "payment_id",  limit: 4
    t.string   "name",        limit: 255
    t.binary   "file",        limit: 65535
    t.string   "extension",   limit: 255
    t.text     "description", limit: 65535
    t.integer  "size",        limit: 4
    t.datetime "created_at",                null: false
    t.datetime "updated_at",                null: false
  end

  add_index "payment_documents", ["payment_id"], name: "index_payment_documents_on_payment_id", using: :btree

  create_table "payments", force: :cascade do |t|
    t.decimal  "value",                      precision: 10, scale: 2
    t.date     "paymentDate"
    t.date     "confirmationDate"
    t.boolean  "status",           limit: 1,                          default: false
    t.datetime "created_at",                                                          null: false
    t.datetime "updated_at",                                                          null: false
  end

  create_table "recover_passwords", force: :cascade do |t|
    t.string   "token",      limit: 255
    t.date     "validity"
    t.datetime "created_at",             null: false
    t.datetime "updated_at",             null: false
  end

  add_index "recover_passwords", ["token"], name: "index_recover_passwords_on_token", unique: true, using: :btree

  create_table "regions", force: :cascade do |t|
    t.string   "name",       limit: 255
    t.string   "acronym",    limit: 255
    t.datetime "created_at",             null: false
    t.datetime "updated_at",             null: false
  end

  add_index "regions", ["acronym"], name: "index_regions_on_acronym", unique: true, using: :btree
  add_index "regions", ["name"], name: "index_regions_on_name", unique: true, using: :btree

  create_table "states", force: :cascade do |t|
    t.integer  "region_id",  limit: 4
    t.string   "name",       limit: 255
    t.string   "acronym",    limit: 255
    t.datetime "created_at",             null: false
    t.datetime "updated_at",             null: false
  end

  add_index "states", ["acronym"], name: "index_states_on_acronym", unique: true, using: :btree
  add_index "states", ["name"], name: "index_states_on_name", unique: true, using: :btree
  add_index "states", ["region_id"], name: "index_states_on_region_id", using: :btree

  create_table "sub_agreement_installments", force: :cascade do |t|
    t.integer  "agreement_installment_id", limit: 4
    t.integer  "payment_id",               limit: 4
    t.decimal  "value",                              precision: 10, scale: 2
    t.boolean  "status",                   limit: 1,                          default: false
    t.datetime "created_at",                                                                  null: false
    t.datetime "updated_at",                                                                  null: false
  end

  add_index "sub_agreement_installments", ["agreement_installment_id"], name: "index_sub_agreement_installments_on_agreement_installment_id", using: :btree
  add_index "sub_agreement_installments", ["payment_id"], name: "index_sub_agreement_installments_on_payment_id", using: :btree

  create_table "user_has_contributors", id: false, force: :cascade do |t|
    t.integer  "vendor_user_id",      limit: 4
    t.integer  "contributor_user_id", limit: 4
    t.datetime "created_at",                    null: false
    t.datetime "updated_at",                    null: false
  end

  add_index "user_has_contributors", ["contributor_user_id"], name: "index_user_has_contributors_on_contributor_user_id", using: :btree
  add_index "user_has_contributors", ["vendor_user_id"], name: "index_user_has_contributors_on_vendor_user_id", using: :btree

  create_table "user_levels", force: :cascade do |t|
    t.string   "levelType",  limit: 255
    t.datetime "created_at",             null: false
    t.datetime "updated_at",             null: false
  end

  add_index "user_levels", ["levelType"], name: "index_user_levels_on_levelType", unique: true, using: :btree

  create_table "users", force: :cascade do |t|
    t.integer  "user_level_id",   limit: 4
    t.integer  "city_organ_id",   limit: 4
    t.string   "name",            limit: 255
    t.string   "email",           limit: 255
    t.string   "phone",           limit: 255
    t.string   "cellPhone",       limit: 255
    t.string   "login",           limit: 255
    t.boolean  "status",          limit: 1,   default: false
    t.string   "cpf",             limit: 255
    t.integer  "maxSalesAmount",  limit: 4
    t.boolean  "isVerified",      limit: 1,   default: false
    t.datetime "created_at",                                  null: false
    t.datetime "updated_at",                                  null: false
    t.string   "password_digest", limit: 255
  end

  add_index "users", ["city_organ_id"], name: "index_users_on_city_organ_id", using: :btree
  add_index "users", ["cpf"], name: "index_users_on_cpf", unique: true, using: :btree
  add_index "users", ["email"], name: "index_users_on_email", unique: true, using: :btree
  add_index "users", ["login"], name: "index_users_on_login", unique: true, using: :btree
  add_index "users", ["user_level_id"], name: "index_users_on_user_level_id", using: :btree

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
