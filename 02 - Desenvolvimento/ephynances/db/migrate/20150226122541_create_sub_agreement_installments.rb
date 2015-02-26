# encoding: UTF-8
class CreateSubAgreementInstallments < ActiveRecord::Migration
  def change
    create_table :sub_agreement_installments do |t|
      t.belongs_to :agreement_installment, index:true
      t.belongs_to :payment, index:true, unique:true
      t.decimal :value, scale: 2, precision: 10
      t.boolean :status, default:false

      t.timestamps null: false;
    end
  end
end
