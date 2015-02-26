class CreateAgreementInstallments < ActiveRecord::Migration
  def change
    create_table :agreement_installments do |t|
      t.belongs_to :agreement, index:true
      t.belongs_to :payment, index:true, unique:true
      t.decimal :value, scale: 2, precision: 10
      t.boolean :status, default:false
      t.date :paymentDate
      t.date :dueDate
      t.date :confirmationDate

      t.timestamps null: false;
    end
  end
end
