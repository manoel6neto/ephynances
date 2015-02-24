class CreateRegions < ActiveRecord::Migration
  def change
    create_table :regions do |t|
      t.string :name
      t.string :acronym

      t.index :name, unique:true
      t.index :acronym, unique:true

      t.timestamps null: false;
    end
  end
end
