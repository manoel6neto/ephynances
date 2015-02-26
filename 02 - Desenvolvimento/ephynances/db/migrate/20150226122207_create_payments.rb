# encoding: UTF-8
class CreatePayments < ActiveRecord::Migration
  def change
    create_table :payments do |t|
      t.decimal :value, scale: 2, precision: 10
      t.date :paymentDate
      t.date :confirmationDate
      t.boolean :status, default:false

      t.timestamps null: false;
    end
  end
end
