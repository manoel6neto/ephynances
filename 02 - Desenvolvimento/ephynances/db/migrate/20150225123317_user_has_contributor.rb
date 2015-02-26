# encoding: UTF-8
class UserHasContributor < ActiveRecord::Migration
  def change
    create_table :user_has_contributors, id:false do |t|
      t.integer :vendor_user_id
      t.integer :contributor_user_id

      t.index :vendor_user_id
      t.index :contributor_user_id

      t.timestamps null: false;
    end
  end
end
