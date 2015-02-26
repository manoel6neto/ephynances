# encoding: UTF-8
class AgreementType < ActiveRecord::Base

  validates_presence_of :agreementType
  validates_uniqueness_of :agreementType

  has_many :agreements
end
