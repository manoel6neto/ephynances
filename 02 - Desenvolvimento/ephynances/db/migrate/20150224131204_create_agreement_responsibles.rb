class CreateAgreementResponsibles < ActiveRecord::Migration
  def change
    create_table :agreement_responsibles do |t|
      t.string :name
      t.string :email
      t.string :phone
      t.string :cellPhone
      t.string :cpf

      t.timestamps null: false
    end
  end
end
