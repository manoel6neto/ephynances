class CreateRecoverPasswords < ActiveRecord::Migration
  def change
    create_table :recover_passwords do |t|
      t.string :token
      t.date :validity

      t.index :token, unique:true

      t.timestamps null: false;
    end
  end
end
