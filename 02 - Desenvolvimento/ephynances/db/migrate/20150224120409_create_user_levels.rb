class CreateUserLevels < ActiveRecord::Migration
  def change
    create_table :user_levels do |t|
      t.string :levelType

      t.index :levelType, unique:true

      t.timestamps null: false
    end
  end
end
