class CreateAgreementDocuments < ActiveRecord::Migration
  def change
    create_table :agreement_documents do |t|
      t.string :name
      t.text :description, limit: 500
      t.integer :size
      t.binary :file

      t.timestamps null: false
    end
  end
end
