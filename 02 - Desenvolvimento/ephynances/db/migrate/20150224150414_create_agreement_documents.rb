# encoding: UTF-8
class CreateAgreementDocuments < ActiveRecord::Migration
  def change
    create_table :agreement_documents do |t|
      t.belongs_to :agreement, index:true
      t.string :name
      t.string :extension
      t.text :description, limit: 500
      t.integer :size
      t.binary :file

      t.timestamps null: false;
    end
  end
end
