class RecoverPassword < ActiveRecord::Base

  validates_presence_of :token, :validity
  validates_uniqueness_of :token
end
