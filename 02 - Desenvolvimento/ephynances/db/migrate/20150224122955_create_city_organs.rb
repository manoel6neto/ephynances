class CreateCityOrgans < ActiveRecord::Migration
  def change
    create_table :city_organs do |t|
      t.string :cnpj
      t.string :cityName
      t.string :organName

      t.index :cnpj, unique:true

      t.timestamps null: false
    end
  end
end
