class CreateUsers < ActiveRecord::Migration
  def change
    create_table :users do |t|
      t.string :name
      t.string :email
      t.string :phone
      t.string :cellPhone
      t.string :login
      t.boolean :status, default:false
      t.string :cpf
      t.integer :maxSalesAmount
      t.boolean :isVerified, default:false

      t.index :email, unique:true
      t.index :cpf, unique:true
      t.index :login, unique:true

      t.timestamps null: false
    end
  end
end
