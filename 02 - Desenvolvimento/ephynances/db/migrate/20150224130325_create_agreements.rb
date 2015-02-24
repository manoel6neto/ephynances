class CreateAgreements < ActiveRecord::Migration
  def change
    create_table :agreements do |t|
      t.string :contactEmail
      t.string :contactPhone
      t.decimal :totalPrice, scale: 2, precision: 10
      t.string :contractorAgreementNumber
      t.string :physisAgreementNumber
      t.date :expiryDate
      t.boolean :status, default:false
      t.integer :cnpjAmount, default: 0
      t.string :documentNumber

      t.index :contractorAgreementNumber, unique:true
      t.index :physisAgreementNumber, unique:true

      t.timestamps null: false
    end
  end
end
