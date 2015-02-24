class UserLevel < ActiveRecord::Base

  validates_presence_of :levelType
  validates_uniqueness_of :levelType
end
