class CreatePaymentDocuments < ActiveRecord::Migration
  def change
    create_table :payment_documents do |t|
      t.belongs_to :payment, index:true
      t.string :name
      t.binary :file
      t.string :extension
      t.text :description, limit: 500
      t.integer :size

      t.timestamps null: false;
    end
  end
end
