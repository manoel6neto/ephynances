class CreateAgreementTypes < ActiveRecord::Migration
  def change
    create_table :agreement_types do |t|
      t.string :type

      t.index :type, unique:true

      t.timestamps null: false;
    end
  end
end
