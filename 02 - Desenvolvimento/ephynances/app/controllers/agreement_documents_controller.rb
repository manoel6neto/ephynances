class AgreementDocumentsController < ApplicationController
  before_action :set_agreement_document, only: [:show, :edit, :update, :destroy]

  # GET /agreement_documents
  # GET /agreement_documents.json
  def index
    @agreement_documents = AgreementDocument.all
  end

  # GET /agreement_documents/1
  # GET /agreement_documents/1.json
  def show
  end

  # GET /agreement_documents/new
  def new
    @agreement_document = AgreementDocument.new
  end

  # GET /agreement_documents/1/edit
  def edit
  end

  # POST /agreement_documents
  # POST /agreement_documents.json
  def create
    @agreement_document = AgreementDocument.new(agreement_document_params)

    respond_to do |format|
      if @agreement_document.save
        format.html { redirect_to @agreement_document, notice: 'Agreement document was successfully created.' }
        format.json { render :show, status: :created, location: @agreement_document }
      else
        format.html { render :new }
        format.json { render json: @agreement_document.errors, status: :unprocessable_entity }
      end
    end
  end

  # PATCH/PUT /agreement_documents/1
  # PATCH/PUT /agreement_documents/1.json
  def update
    respond_to do |format|
      if @agreement_document.update(agreement_document_params)
        format.html { redirect_to @agreement_document, notice: 'Agreement document was successfully updated.' }
        format.json { render :show, status: :ok, location: @agreement_document }
      else
        format.html { render :edit }
        format.json { render json: @agreement_document.errors, status: :unprocessable_entity }
      end
    end
  end

  # DELETE /agreement_documents/1
  # DELETE /agreement_documents/1.json
  def destroy
    @agreement_document.destroy
    respond_to do |format|
      format.html { redirect_to agreement_documents_url, notice: 'Agreement document was successfully destroyed.' }
      format.json { head :no_content }
    end
  end

  private
    # Use callbacks to share common setup or constraints between actions.
    def set_agreement_document
      @agreement_document = AgreementDocument.find(params[:id])
    end

    # Never trust parameters from the scary internet, only allow the white list through.
    def agreement_document_params
      params[:agreement_document]
    end
end
