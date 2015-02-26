# encoding: UTF-8
class Agreement < ActiveRecord::Base

  validates_presence_of :totalPrice, :contractorAgreementNumber, :physisAgreementNumber, :expiryDate, :status
  validates_uniqueness_of :contractorAgreementNumber, :physisAgreementNumber

  has_many :agreement_installments
  has_many :agreement_documents
  belongs_to :agreement_responsible
  belongs_to :agreement_type
  belongs_to :user
end
