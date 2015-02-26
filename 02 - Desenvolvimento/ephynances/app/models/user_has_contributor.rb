# encoding: UTF-8
class UserHasContributor < ActiveRecord::Base

  validates_presence_of :vendor_user_id, :contributor_user_id

end