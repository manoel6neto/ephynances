class AgreementType < ActiveRecord::Base

  validates_presence_of :type
  validates_uniqueness_of :type

  has_many :agreements
end
