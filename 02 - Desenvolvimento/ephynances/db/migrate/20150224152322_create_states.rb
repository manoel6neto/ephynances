class CreateStates < ActiveRecord::Migration
  def change
    create_table :states do |t|
      t.belongs_to :region, index:true
      t.string :name
      t.string :acronym

      t.index :name, unique:true
      t.index :acronym, unique:true

      t.timestamps null: false;
    end
  end
end
