# encoding: UTF-8
class CreateAgreementTypes < ActiveRecord::Migration
  def change
    create_table :agreement_types do |t|
      t.string :agreementType

      t.index :agreementType, unique:true

      t.timestamps null: false;
    end
  end
end
